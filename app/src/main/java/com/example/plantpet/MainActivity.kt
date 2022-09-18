package com.example.plantpet

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.plantpet.databinding.FragmentSensorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private var bindSensor : FragmentSensorBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        bindSensor = FragmentSensorBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main)
        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("sensor/arduinocode/")
//        binding.logoutBtn.setOnClickListener {
//            auth.signOut()
//            val intent = Intent(this, IntroActivity::class.java)
//            //뒤로가기할때 회원가입화면 안나오게하기
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pot_temperature = dataSnapshot.child("pot_temperature").value
                Toast.makeText(baseContext, "값 가져오기", Toast.LENGTH_LONG).show()
                bindSensor!!.temptext.text = pot_temperature.toString() + "°C"
            }
            override fun onCancelled(error: DatabaseError) {  // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })







    }
}