#include <Firebase_Arduino_WiFiNINA.h>
#include <DHT.h>
#define FIREBASE_HOST "plantpet-487ea-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "vhH8P7vi8qtzprtCuQNvgxsrGWk68908LMlRX0TN"
#define WIFI_SSID "손명균의 iPhone"
#define WIFI_PASSWORD "streetcat1998"

//아날로그 0번핀에 토양습도 센서
#define A0Pin 0
//아날로그 1번핀 조도 센서
#define A1Pin 1


//모터제어 핀 할당
int motorA1 = 0;
int motorA2 = 1;


//디지털 2번핀에 dht11
#define DHTPIN 2
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

//LED제어 핀 할당
int pot_LED = 3;
int count = 0;
FirebaseData firebaseData;

String path = "/sensor/arduino0001";
String jsonStr;

void setup()
{
  dht.begin();
  
  pinMode(motorA1, OUTPUT);
  pinMode(motorA2, OUTPUT);

  pinMode(pot_LED, OUTPUT);
  
  Serial.begin(9600);
  delay(1000);
  Serial.print("Connecting to WiFi…");
  int status = WL_IDLE_STATUS;
  while (status != WL_CONNECTED) {
    status = WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print(".");
    delay(300);
  }
  Serial.print(" IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH, WIFI_SSID, WIFI_PASSWORD);
  Firebase.reconnectWiFi(true);

  if(Firebase.getString(firebaseData, path + "/POT_LED")){
    Serial.println("This device has previously accessed the database");
  }
  else{
    if(Firebase.setString(firebaseData, path + "/POT_LED", "0")){
      Serial.println("First time access");
    }
    else{
    Serial.println("Error: " + firebaseData.errorReason());
    }
    if(Firebase.setString(firebaseData, path + "/POT_PUMP", "0")){
      Serial.println("complete");
    }
    else{
    Serial.println("Error: " + firebaseData.errorReason());
    }
    if(Firebase.setString(firebaseData, path + "/AUTO_ACT", "0")){
      Serial.println("complete");
    }
    else{
    Serial.println("Error: " + firebaseData.errorReason());
    }
    if(Firebase.setInt(firebaseData, path + "/light_value", 50)){
      Serial.println("complete");
    }
    else{
    Serial.println("Error: " + firebaseData.errorReason());
    }
    if(Firebase.setInt(firebaseData, path + "/limit_light", 50)){
      Serial.println("complete");
    }
    else{
    Serial.println("Error: " + firebaseData.errorReason());
    }
    if(Firebase.setInt(firebaseData, path + "/limit_soil", 50)){
      Serial.println("complete");
    }
    else{
    Serial.println("Error: " + firebaseData.errorReason());
    }
  }
}

void loop()
{
  Serial.println("------------------------------------------------------");
  //토양습도 읽어오기
  int soil_humidity = map(analogRead(A0Pin), 1023 , 850, 0, 100);
  //화분 온습도 읽어오기
  float pot_temp = dht.readTemperature();
  float pot_humi = dht.readHumidity();
  float f = dht.readTemperature(true);
  //조도 읽어오기
  int light = map(analogRead(A1Pin), 0, 1023, 0, 100);
  
  delay(1000);
  //토양습도 출력
  Serial.print("Soil_humidity = ");
  Serial.println(soil_humidity);
  //화분 온습도 출력
  Serial.print(F("Humidity: "));
  Serial.print(pot_humi);
  Serial.print(F("% \nTemperature: "));
  Serial.print(pot_temp);
  Serial.print(F("°C "));
  Serial.print(f);
  Serial.println();
  //조도 출력
  Serial.print("Light = ");
  Serial.println(light);

  delay(100);
  if(Firebase.getString(firebaseData, path + "/AUTO_ACT")){
    String auto_value = firebaseData.stringData();
    //auto act 값이 1일경우
    if(auto_value == "1"){
      //LED동작 지정
      Serial.println("auto act on");
      if(Firebase.getInt(firebaseData, path + "/limit_light")){
        int limit_light = firebaseData.intData();
        Serial.print("limit light : ");
        Serial.println(limit_light);
        if(light < limit_light){
          if(Firebase.getInt(firebaseData, path + "/light_value")){
            int light_value = firebaseData.intData();
            Serial.print("light value : ");
            Serial.println(light_value);
            light_value = map(light_value, 0, 100, 0, 255);
            analogWrite(pot_LED, light_value);
          }
        }
        else{
          digitalWrite(pot_LED, LOW);
        }
      }
      else{
        Serial.println("Error: " + firebaseData.errorReason());
      }
      if(Firebase.getInt(firebaseData, path + "/limit_soil")){
        int limit_soil = firebaseData.intData();
        Serial.print("limit soil : ");
        Serial.println(limit_soil);
        if(soil_humidity < limit_soil){
          //펌프동작 지정
          Serial.println("Moter On");
          digitalWrite(motorA1, HIGH);
          digitalWrite(motorA2, LOW);
        }
        else{
          //펌프정지
          Serial.println("Moter Off");
          digitalWrite(motorA1, LOW);
          digitalWrite(motorA2, LOW);
        }
      }
      else{
        Serial.println("Error: " + firebaseData.errorReason());
      }
    }
    //auto act 값이 0일경우
    else if(auto_value == "0"){
      //LED 동작 지정
      if(Firebase.getString(firebaseData, path + "/POT_LED")){
        String led_value = firebaseData.stringData();
        Serial.print("POT LED : ");
        Serial.println(led_value);
        if(led_value == "1"){
          digitalWrite(pot_LED, HIGH);
        }
        else{
          digitalWrite(pot_LED, LOW);
        }
      }
      else{
        Serial.println("Error: " + firebaseData.errorReason());
      }
      //pump동작지정값 확인
      if(Firebase.getString(firebaseData, path + "/POT_PUMP")){
        String pump_value = firebaseData.stringData();
        Serial.print("POT PUMP : "); 
        Serial.println(pump_value);
        if(pump_value == "1"){
          if(count == 0){
            //3초동안 펌프동작 지정
            count = 1;
            Serial.println("Moter On");
            digitalWrite(motorA1, HIGH);
            digitalWrite(motorA2, LOW);
          }
          else{
            count = count - 1;
            if(count == 0){
              //펌프정지
              Serial.println("Moter Off");
              digitalWrite(motorA1, LOW);
              digitalWrite(motorA2, LOW);
              //파이어베이스에 펌프동작값 0으로 변경
              if(Firebase.setString(firebaseData, path + "/POT_PUMP", "0")){
                Serial.println("PUMP OFF");
              }
            }
          }
        }
        else{
          Serial.println("Moter Off");
          digitalWrite(motorA1, LOW);
          digitalWrite(motorA2, LOW);
        }
      }
      else{
        Serial.println("Error: " + firebaseData.errorReason());
      }
    }
    //auto act값이 0,1이 아닐경우 에러메시지 출력
    else{
      Serial.println("auto_act value error!");
    }
  }
  else{
    Serial.println("Error: " + firebaseData.errorReason());
  }

  //센서값들 파이어베이스로 전달
  if(Firebase.setInt(firebaseData, path + "/soil_humidity", soil_humidity)){
    Serial.println(firebaseData.dataPath() + " = " + soil_humidity);
  }
  if(Firebase.setInt(firebaseData, path + "/light", light)){
    Serial.println(firebaseData.dataPath() + " = " + light);
  }
  if(Firebase.setFloat(firebaseData, path + "/pot_temperature", pot_temp)){
    Serial.println(firebaseData.dataPath() + " = " + pot_temp);
  }
  if(Firebase.setFloat(firebaseData, path + "/pot_humidity", pot_humi)){
    Serial.println(firebaseData.dataPath() + " = " + pot_humi);
  }
//  //json타입으로 센서값 기록
//  jsonStr = "{\"soil_humi\":" + String(soil_humidity,6) + ",\"pot_humi\":" + String(pot_humi,6);
//  jsonStr += ",\"pot_temp\":" + String(pot_temp,6)+ ",\"light\":" + String(light,6) + "}";
//  if (Firebase.pushJSON(firebaseData, path + "/2-pushJSON", jsonStr)) {
//      Serial.println(firebaseData.dataPath() + " = " + firebaseData.pushName());
//    }
//    else {
//      Serial.println("Error: " + firebaseData.errorReason());
//    }
  Serial.println("------------------------------------------------------");
}
