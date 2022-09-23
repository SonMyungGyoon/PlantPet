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
import com.example.plantpet.databinding.FragmentSensorBinding
import com.example.plantpet.setting.SensorSettingActivity
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sensor, container, false)
        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("sensor/arduinocode")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pot_temperature = dataSnapshot.child("pot_temperature").value
                val pot_humidity = dataSnapshot.child("pot_humidity").value
                val pot_light = dataSnapshot.child("light").value
                val pot_soil = dataSnapshot.child("soil_humidity").value
                val led = dataSnapshot.child("POT_LED").value
                val pump = dataSnapshot.child("POT_PUMP").value
                val autoact = dataSnapshot.child("AUTO_ACT").value
                binding.temptext.text = pot_temperature.toString() + "°C"
                binding.humitext.text = pot_humidity.toString() + "%"
                binding.lighttext.text = pot_light.toString() + "%"
                binding.soiltext.text = pot_soil.toString() + "%"
                binding.ledswitch.isChecked = led == "1"
                binding.pumpswitch.isChecked = pump == "1"
                binding.autoact.isChecked = autoact == "1"
            }
            override fun onCancelled(error: DatabaseError) {  // Failed to read value
            }
        })

        binding.ledswitch.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                myRef.child("POT_LED").setValue("1")
            }
            else{
                myRef.child("POT_LED").setValue("0")
            }
        }
        binding.pumpswitch.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked){
                myRef.child("POT_PUMP").setValue("1")
            }
            else{
                myRef.child("POT_PUMP").setValue("0")
            }
        }

        binding.autoact.setOnCheckedChangeListener{ buttonView, isChecked ->
            binding.ledswitch.isEnabled = !isChecked
            binding.pumpswitch.isEnabled = !isChecked
            if(isChecked){
                myRef.child("AUTO_ACT").setValue("1")
            }
            else{
                myRef.child("AUTO_ACT").setValue("0")
            }
        }

        binding.sensorsetBtn.setOnClickListener {
            val intent = Intent(context, SensorSettingActivity::class.java)
            startActivity(intent)
        }
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
    

}