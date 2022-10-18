package com.example.plantpet.board

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.plantpet.FB.FBAuth
import com.example.plantpet.R

class HomeBoardLVAdapter(val boardList : MutableList<BoardModel>) : BaseAdapter() {

    override fun getCount(): Int {

        return boardList.size

        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any {

        return boardList[position]
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var con = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item2,parent, false)

        val itemColor = con?.findViewById<LinearLayout>(R.id.itemView)
        val title = con?.findViewById<TextView>(R.id.titleArea)

        title!!.text = boardList[position].title

        if(boardList[position].uid.equals(FBAuth.getUid())){
            itemColor?.setBackgroundColor(Color.parseColor("#FAFAD2"))
        }
        return con!!
        TODO("Not yet implemented")
    }
}