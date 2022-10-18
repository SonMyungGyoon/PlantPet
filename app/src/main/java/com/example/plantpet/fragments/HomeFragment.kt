package com.example.plantpet.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.plantpet.FB.FBAuth
import com.example.plantpet.FB.FBRef
import com.example.plantpet.R
import com.example.plantpet.board.BoardModel
import com.example.plantpet.board.HomeBoardLVAdapter
import com.example.plantpet.board.PostActivity
import com.example.plantpet.databinding.FragmentHomeBinding
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    private val TAG = TalkFragment::class.java.simpleName

    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()


    private lateinit var adap : HomeBoardLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        adap = HomeBoardLVAdapter(boardDataList)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.boardListView.adapter = adap

        binding.boardListView.setOnItemClickListener{parent, view, position, id ->
            val intent = Intent(context, PostActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)
        }

        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val userRef : DatabaseReference = database.getReference("userData/" + FBAuth.getUid())
        System.out.println("checkpoint1 path : " + "userData/" + FBAuth.getUid())
        var sensorCode = ""
        userRef.child("sensorId").get().addOnSuccessListener{
            sensorCode = it.value.toString()
            System.out.println("checkpoint2 path : " + sensorCode)
            System.out.println("checkpoint3 path : " + it.value.toString())
            var myRef : DatabaseReference = database.getReference("sensor/"+sensorCode)
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val pot_temperature = dataSnapshot.child("pot_temperature").value
                    val pot_humidity = dataSnapshot.child("pot_humidity").value
                    val pot_light = dataSnapshot.child("light").value
                    val pot_soil = dataSnapshot.child("soil_humidity").value
                    binding.temptext.text = pot_temperature.toString() + "°C"
                    binding.humitext.text = pot_humidity.toString() + "%"
                    binding.lighttext.text = pot_light.toString() + "%"
                    binding.soiltext.text = pot_soil.toString() + "%"
                }
                override fun onCancelled(error: DatabaseError) {  // Failed to read value
                }
            })
        }

        binding.sensortap.setOnClickListener{
            //Toast.makeText(context, "sensortap clicked", Toast.LENGTH_LONG).show()
            it.findNavController().navigate(R.id.action_homeFragment_to_sensorFragment)
        }
        binding.talktap.setOnClickListener{
            //Toast.makeText(context, "sensortap clicked", Toast.LENGTH_LONG).show()
            it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment)
        }
        binding.bookmarktap.setOnClickListener{
            //Toast.makeText(context, "sensortap clicked", Toast.LENGTH_LONG).show()
            it.findNavController().navigate(R.id.action_homeFragment_to_bookmarkFragment)
        }
        binding.storetap.setOnClickListener{
            //Toast.makeText(context, "sensortap clicked", Toast.LENGTH_LONG).show()
            it.findNavController().navigate(R.id.action_homeFragment_to_storeFragment)
        }

        getFBBoardData()

        return binding.root
    }

    private fun getFBBoardData(){

        val postListner = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                boardDataList.clear()

                for(dataModel in snapshot.children) {

                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }
                boardDataList.reverse()
                boardKeyList.reverse()
                adap.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.boardRef.addValueEventListener(postListner)
    }

}