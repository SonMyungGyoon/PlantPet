package com.example.plantpet.setting

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.plantpet.FB.FBAuth
import com.example.plantpet.R
import com.example.plantpet.auth.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {

    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        auth = Firebase.auth

        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val userRef : DatabaseReference = database.getReference("userData/" + FBAuth.getUid())

        //val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        val settingIdArea : TextView = findViewById(R.id.settingIdArea)
        var email = FBAuth.getEmail()
        if(email.equals("") || email.equals("null")){
            email = "(익명)"
        }
        settingIdArea.text = "ID : "+email
        val sensorCodeArea : TextView = findViewById(R.id.sensorCodeArea)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                sensorCodeArea.text = "sensorCode : "+ dataSnapshot.child("sensorId").value.toString()
            }
            override fun onCancelled(error: DatabaseError) {  // Failed to read value
            }
        })

        val sensorsetBtn : Button = findViewById(R.id.sensorsetBtn)
        sensorsetBtn.setOnClickListener {
            showSensorDialog()
        }

        val logoutBtn : Button = findViewById(R.id.logoutBtn)
        logoutBtn.setOnClickListener {

            auth.signOut()
            Toast.makeText(this, "로그아웃", Toast.LENGTH_LONG).show()

            val intent = Intent(this, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or  Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }
    }
    private fun showSensorDialog(){
        val mDialogView = layoutInflater.inflate(R.layout.sensorset_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("기기코드 등록")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.submitBtn)?.setOnClickListener {
            val sensorCode =  alertDialog.findViewById<EditText>(R.id.sensorCodeArea).text.toString()
            val uid = FBAuth.getUid()

            System.out.println(sensorCode)
            val database : FirebaseDatabase = FirebaseDatabase.getInstance()
            val userRef : DatabaseReference = database.getReference("userData/"+uid)

            userRef.child("sensorId").setValue(sensorCode)
            alertDialog.dismiss()

        }
        alertDialog.findViewById<Button>(R.id.cancelBtn)?.setOnClickListener {
            alertDialog.dismiss()
        }

    }
}