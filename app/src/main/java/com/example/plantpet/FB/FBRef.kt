package com.example.plantpet.FB

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object{

        val database = Firebase.database

        val boardRef = database.getReference("board")
        val commentRef = database.getReference("comment")
        val userRef = database.getReference("userData")
    }
}