package com.example.plantpet.FB

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class FBAuth {

    companion object{

        private lateinit var  auth: FirebaseAuth

        fun getUid() : String{
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }
        fun getEmail() : String{
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.email.toString()
        }
        fun getTime() : String {
            val time = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss",Locale.KOREA)
                .format(time)

            return dateFormat
        }

        fun getSensorId() : String{
            auth = FirebaseAuth.getInstance()
            val database : FirebaseDatabase = FirebaseDatabase.getInstance()
            val userRef : DatabaseReference = database.getReference("userData/" + auth.currentUser?.uid.toString())
            var sensorCode : String = ""
            userRef.child("sensorId").get().addOnSuccessListener{
                sensorCode = it.value.toString()
            }
            return sensorCode
        }

        fun getPath() :String{
            return getSensorId()
        }

    }
}