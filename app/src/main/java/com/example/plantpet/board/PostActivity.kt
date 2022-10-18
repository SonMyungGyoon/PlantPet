package com.example.plantpet.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.plantpet.FB.FBAuth
import com.example.plantpet.FB.FBRef
import com.example.plantpet.FB.FBRef.Companion.database
import com.example.plantpet.R
import com.example.plantpet.comment.CommentLVAdapter
import com.example.plantpet.comment.CommentModel
import com.example.plantpet.databinding.ActivityPostBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class PostActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPostBinding

    private lateinit var key : String

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter : CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post)

        binding.postControlIcon.setOnClickListener{
            ShowDialog()
        }

        key = intent.getStringExtra("key").toString()

        getBoardData(key)
        getImageData(key)

        binding.commentBtn.setOnClickListener{
            insertComment()
        }

        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentAdapter

        getCommentData(key)

    }

    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                try {
                    val dataModel = snapshot.getValue(BoardModel::class.java)

                    binding.titleArea.text = dataModel!!.title
                    binding.contentArea.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time

                    val myUid = FBAuth.getUid()

                    if(myUid.equals(dataModel.uid)){
                        binding.postControlIcon.isVisible = true
                    }
                } catch (e : Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PostActivity,"게시글을 불러오는데 실패했습니다.",Toast.LENGTH_SHORT).show()
            }

        }

        FBRef.boardRef.child(key).addValueEventListener(postListener)


    }

    private fun getImageData(key : String){

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageView = binding.getImageArea

        // Download directly from   using Glide
        // (See MyAppGlideModule for Loader registration)
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener{ task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageView)
            }
            else{
                binding.getImageArea.isVisible = false
            }
        })

    }

    fun getCommentData(key : String){
        val postListner = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                commentDataList.clear()
                for(dataModel in snapshot.children) {

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                    commentDataList
                }

                commentAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListner)
    }

    fun insertComment(){
        //comment
        // -boardkey
        //      - commentKey
        //          -commentData

        FBRef.commentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(
                    FBAuth.getEmail(),
                    binding.commentArea.text.toString(),
                    FBAuth.getTime()
                )
            )

        Toast.makeText(this,"댓글입력 완료", Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")
    }

    private fun ShowDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

       val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {

            val intent = Intent(this,EditActivity::class.java)
            intent.putExtra("key",key)
            startActivity(intent)

        }
        alertDialog.findViewById<Button>(R.id.delBtn)?.setOnClickListener {
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this,"게시글 삭제 완료",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}