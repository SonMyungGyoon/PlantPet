package com.example.plantpet.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.plantpet.FB.FBAuth
import com.example.plantpet.FB.FBRef
import com.example.plantpet.R
import com.example.plantpet.databinding.ActivityEditBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class EditActivity : AppCompatActivity() {

    private lateinit var key : String

    private lateinit var binding : ActivityEditBinding

    private lateinit var uid : String

    private var isImage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)


            key = intent.getStringExtra("key").toString()

            getBoardData(key)
            getImageData(key)

        binding.editBtn.setOnClickListener {
            EditBoardData(key)
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        binding.imageArea.setOnClickListener{

            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImage = true

        }
    }

    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val dataModel = snapshot.getValue(BoardModel::class.java)

                binding.titleArea.setText(dataModel?.title)
                binding.contentArea.setText(dataModel?.content)
                uid = dataModel!!.uid


            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        FBRef.boardRef.child(key).addValueEventListener(postListener)


    }

    private fun getImageData(key : String){

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageView = binding.imageArea

        // Download directly from   using Glide
        // (See MyAppGlideModule for Loader registration)
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener{ task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageView)
            }
            else{

            }
        })

    }

    private fun EditBoardData(key : String) {

        FBRef.boardRef
            .child(key)
            .setValue(
                BoardModel(
                    binding.titleArea.text.toString(),
                    binding.contentArea.text.toString(),
                    uid,
                    FBAuth.getTime()
                )
            )
        Toast.makeText(this,"게시글이 수정됐습니다.",Toast.LENGTH_SHORT).show()

        finish()
    }
}