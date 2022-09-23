package com.example.plantpet.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.plantpet.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("sensor/arduinocode")
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


        return binding.root
    }

}