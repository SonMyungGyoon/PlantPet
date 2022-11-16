package com.example.plantpet.comment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.plantpet.FB.FBAuth
import com.example.plantpet.R

class CommentLVAdapter(val commentList : MutableList<CommentModel>) : BaseAdapter(){
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item,parent, false)
        }

        val title = view?.findViewById<TextView>(R.id.titleArea)
        val content = view?.findViewById<TextView>(R.id.contentArea)
        val time = view?.findViewById<TextView>(R.id.timeArea)
        if(commentList[position].commentUser.equals((""))){
            title!!.text = "(익명)"
        }
        else{
            title!!.text = commentList[position].commentUser
        }
        content!!.text = commentList[position].commentContent
        time!!.text = commentList[position].commentCreatedTime

        return view!!
    }

}