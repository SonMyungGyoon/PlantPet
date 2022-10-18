package com.example.plantpet.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.plantpet.FB.FBAuth
import com.example.plantpet.FB.FBRef
import com.example.plantpet.FB.UserModel
import com.example.plantpet.MainActivity
import com.example.plantpet.R
import com.example.plantpet.board.BoardModel
import com.example.plantpet.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class JoinActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)
        binding.joinBtn.setOnClickListener {

            var isGoToJoin = true

            val email = binding.emailArea.text.toString()
            val password1 = binding.passwordArea1.text.toString()
            val password2 = binding.passwordArea2.text.toString()

            if(email.isEmpty()){
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if(password1.isEmpty()){
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if(password2.isEmpty()){
                Toast.makeText(this, "비밀번호 확인을 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            //비밀번호 비교
            if(!password1.equals(password2)){
                Toast.makeText(this, "비밀번호값이 다릅니다.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            //비밀번호 자릿수확인
            if(password1.length<6){
                Toast.makeText(this, "비밀번호를 6자리 이상 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(isGoToJoin){
                auth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(this, "성공", Toast.LENGTH_LONG).show()

                            val uid = FBAuth.getUid()
                            FBRef.userRef
                                .child(uid)
                                .setValue(UserModel(email, ""))

                            val intent = Intent(this, MainActivity::class.java)
                            //뒤로가기할때 회원가입화면 안나오게하기
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "이미 존재하는 이메일입니다.", Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }


    }
}