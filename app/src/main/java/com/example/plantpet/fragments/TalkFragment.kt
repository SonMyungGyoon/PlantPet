package com.example.plantpet.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.plantpet.R
import com.example.plantpet.board.BoardListLVAdapter
import com.example.plantpet.board.BoardModel
import com.example.plantpet.board.BoardWriteACtivity
import com.example.plantpet.board.PostActivity
import com.example.plantpet.databinding.FragmentTalkBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TalkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TalkFragment : Fragment() {

    private lateinit var binding : FragmentTalkBinding
    private val TAG = TalkFragment::class.java.simpleName
    private val boardDataList = mutableListOf<BoardModel>()
    private lateinit var adap : BoardListLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)

        adap = BoardListLVAdapter(boardDataList)

        binding.boardListView.adapter = adap

        binding.boardListView.setOnItemClickListener{parent, view, position, id ->
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra("title", boardDataList[position].title) //타이틀
            intent.putExtra("content", boardDataList[position].content) //내용
            intent.putExtra("time", boardDataList[position].time) //시간
            startActivity(intent)
        }

        binding.writeBtn.setOnClickListener{
            val intent = Intent(context, BoardWriteACtivity::class.java)
            startActivity(intent)
        }

        binding.hometap.setOnClickListener{
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }
        binding.sensortap.setOnClickListener{
            it.findNavController().navigate(R.id.action_talkFragment_to_sensorFragment)
        }
        binding.bookmarktap.setOnClickListener{
            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)
        }
        binding.storetap.setOnClickListener{
            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)
        }
        getFBBoardData()

        return binding.root
    }

    private fun getFBBoardData(){

        val postListner = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
                boardDataList.clear()
                for(dataModel in snapshot.children) {
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                }
                boardDataList.reverse()
                adap.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
    }

}