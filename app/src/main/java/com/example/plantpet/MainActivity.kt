package com.example.plantpet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantpet.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.logoutBtn.setOnClickListener {
//            auth.signOut()
//            val intent = Intent(this, IntroActivity::class.java)
//            //뒤로가기할때 회원가입화면 안나오게하기
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }



    }
}