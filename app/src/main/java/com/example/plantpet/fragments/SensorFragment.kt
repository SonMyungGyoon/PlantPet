package com.example.plantpet.fragments

import android.content.ContentValues
import android.database.DatabaseUtils
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.findNavController
import com.example.plantpet.R
import com.example.plantpet.databinding.FragmentSensorBinding
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SensorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SensorFragment : Fragment() {

    private lateinit var binding : FragmentSensorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        //val myRef : DatabaseReference = database.getReference("sensor/arduinocode")

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sensor, container, false)

        val bind = FragmentSensorBinding.inflate(inflater, container, false)

        /*myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pot_temperature = dataSnapshot.child("pot_temperature").value
                bind.temptext.text = pot_temperature.toString() + "°C"
            }
            override fun onCancelled(error: DatabaseError) {  // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })*/

        binding.hometap.setOnClickListener{
            it.findNavController().navigate(R.id.action_sensorFragment_to_homeFragment)
        }
        binding.talktap.setOnClickListener{
            it.findNavController().navigate(R.id.action_sensorFragment_to_talkFragment)
        }
        binding.bookmarktap.setOnClickListener{
            it.findNavController().navigate(R.id.action_sensorFragment_to_bookmarkFragment)
        }
        binding.storetap.setOnClickListener{
            it.findNavController().navigate(R.id.action_sensorFragment_to_storeFragment)
        }

        return binding.root
    }

    fun changeTempText(string: String){
        binding.temptext.text = string
    }

}