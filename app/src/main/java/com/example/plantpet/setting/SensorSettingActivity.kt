package com.example.plantpet.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.plantpet.R
import com.example.plantpet.databinding.ActivitySensorSettingBinding
import com.google.firebase.database.*

class SensorSettingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySensorSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sensor_setting)

        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("sensor/arduinocode")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val limit_ligth = dataSnapshot.child("limit_light").value
                val limit_soil = dataSnapshot.child("limit_soil").value
                val light_value = dataSnapshot.child("light_value").value
                binding.limitlighttext.text = limit_ligth.toString() + "%"
                binding.limitsoiltext.text = limit_soil.toString() + "%"
                binding.ledtext.text = light_value.toString() + "%"
            }
            override fun onCancelled(error: DatabaseError) {  // Failed to read value
            }
        })

        binding.saveBtn.setOnClickListener {
            val limit_light = binding.limitlightval.text.toString().toIntOrNull()
            val limit_soil = binding.limitsoilval.text.toString().toIntOrNull()
            val led_value = binding.ledval.text.toString().toIntOrNull()

            if(limit_light != null && limit_light >= 0 && limit_light <= 100){
                myRef.child("limit_light").setValue(limit_light)
            }
            if(limit_soil != null && limit_soil >= 0 && limit_soil <= 100){
                myRef.child("limit_soil").setValue(limit_soil)
            }
            if(led_value != null && led_value >= 0 && led_value <= 100){
                myRef.child("light_value").setValue(led_value)
            }
        }


    }
}