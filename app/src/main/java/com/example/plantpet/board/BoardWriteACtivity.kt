package com.example.plantpet.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.plantpet.FB.FBAuth
import com.example.plantpet.FB.FBRef
import com.example.plantpet.R
import com.example.plantpet.databinding.ActivityBoardWriteBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BoardWriteACtivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardWriteBinding
    private var isImage = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.writeBtn.setOnClickListener {

            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            val key = FBRef.boardRef.push().key.toString()

            FBRef.boardRef
                .child(key)
                .setValue(BoardModel(title,content, uid, time))


            Toast.makeText(this, "게시글이 등록되었습니다.",Toast.LENGTH_LONG).show()

            if(isImage == true)
                imageUpload(key)

            finish()

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

    private fun imageUpload(key : String){

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef =storageRef.child(key + ".png")
        val imageView = binding.imageArea

        // Get the data from an ImageView as bytes
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100){
            binding.imageArea.setImageURI(data?.data)
        }
    }
}
