package com.example.plantpet.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.plantpet.FB.FBAuth
import com.example.plantpet.FB.FBRef.Companion.database
import com.example.plantpet.MainActivity
import com.example.plantpet.R
import com.example.plantpet.databinding.FragmentSensorBinding
import com.google.firebase.auth.FirebaseAuth
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

    private lateinit var  auth: FirebaseAuth

    private lateinit var binding : FragmentSensorBinding
    private var path = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val userRef : DatabaseReference = database.getReference("userData/" + uid)
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                path = snapshot.child("sensorId").value.toString()
            }
            override fun onCancelled(error: DatabaseError) {  // Failed to read value
            }
        })
        System.out.println(path)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sensor, container, false)
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

                    val limit_ligth = dataSnapshot.child("limit_light").value
                    val limit_soil = dataSnapshot.child("limit_soil").value
                    val light_value = dataSnapshot.child("light_value").value
                    binding.limitlighttext.text = limit_ligth.toString() + "%"
                    binding.limitsoiltext.text = limit_soil.toString() + "%"
                    binding.ledtext.text = light_value.toString() + "%"
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

            binding.saveBtn.setOnClickListener {
                val limit_light = binding.limitlightval.text.toString().toIntOrNull()
                val limit_soil = binding.limitsoilval.text.toString().toIntOrNull()
                val led_value = binding.ledval.text.toString().toIntOrNull()

                if(limit_light != null && limit_light >= 0 && limit_light <= 100){
                    myRef.child("limit_light").setValue(limit_light)
                }
                if(limit_soil != null && limit_soil >= 0 && limit_soil <= 100){
                    myRef.child("limit_soil").setValue(limit_soil)
                }
                if(led_value != null && led_value >= 0 && led_value <= 100){
                    myRef.child("light_value").setValue(led_value)
                }
            }

            binding.sensorsetBtn.setOnClickListener {
                showSensorDialog()
            }
            binding.hometap.setOnClickListener{
                it.findNavController().navigate(R.id.action_sensorFragment_to_homeFragment)
            }
            binding.talktap.setOnClickListener{
                it.findNavController().navigate(R.id.action_sensorFragment_to_talkFragment)
            }
            binding.storetap.setOnClickListener{
                it.findNavController().navigate(R.id.action_sensorFragment_to_storeFragment)
            }
        }
        System.out.println("checkpoint4 path : " + sensorCode)

//        var myRef : DatabaseReference = database.getReference("sensor/"+sensorCode)
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val pot_temperature = dataSnapshot.child("pot_temperature").value
//                val pot_humidity = dataSnapshot.child("pot_humidity").value
//                val pot_light = dataSnapshot.child("light").value
//                val pot_soil = dataSnapshot.child("soil_humidity").value
//                val led = dataSnapshot.child("POT_LED").value
//                val pump = dataSnapshot.child("POT_PUMP").value
//                val autoact = dataSnapshot.child("AUTO_ACT").value
//                binding.temptext.text = pot_temperature.toString() + "°C"
//                binding.humitext.text = pot_humidity.toString() + "%"
//                binding.lighttext.text = pot_light.toString() + "%"
//                binding.soiltext.text = pot_soil.toString() + "%"
//                binding.ledswitch.isChecked = led == "1"
//                binding.pumpswitch.isChecked = pump == "1"
//                binding.autoact.isChecked = autoact == "1"
//
//                val limit_ligth = dataSnapshot.child("limit_light").value
//                val limit_soil = dataSnapshot.child("limit_soil").value
//                val light_value = dataSnapshot.child("light_value").value
//                binding.limitlighttext.text = limit_ligth.toString() + "%"
//                binding.limitsoiltext.text = limit_soil.toString() + "%"
//                binding.ledtext.text = light_value.toString() + "%"
//            }
//            override fun onCancelled(error: DatabaseError) {  // Failed to read value
//            }
//        })
//
//        binding.ledswitch.setOnCheckedChangeListener{ buttonView, isChecked ->
//            if(isChecked){
//                myRef.child("POT_LED").setValue("1")
//            }
//            else{
//                myRef.child("POT_LED").setValue("0")
//            }
//        }
//        binding.pumpswitch.setOnCheckedChangeListener{ buttonView, isChecked ->
//            if(isChecked){
//                myRef.child("POT_PUMP").setValue("1")
//            }
//            else{
//                myRef.child("POT_PUMP").setValue("0")
//            }
//        }
//
//        binding.autoact.setOnCheckedChangeListener{ buttonView, isChecked ->
//            binding.ledswitch.isEnabled = !isChecked
//            binding.pumpswitch.isEnabled = !isChecked
//            if(isChecked){
//                myRef.child("AUTO_ACT").setValue("1")
//            }
//            else{
//                myRef.child("AUTO_ACT").setValue("0")
//            }
//        }
//
//        binding.saveBtn.setOnClickListener {
//            val limit_light = binding.limitlightval.text.toString().toIntOrNull()
//            val limit_soil = binding.limitsoilval.text.toString().toIntOrNull()
//            val led_value = binding.ledval.text.toString().toIntOrNull()
//
//            if(limit_light != null && limit_light >= 0 && limit_light <= 100){
//                myRef.child("limit_light").setValue(limit_light)
//            }
//            if(limit_soil != null && limit_soil >= 0 && limit_soil <= 100){
//                myRef.child("limit_soil").setValue(limit_soil)
//            }
//            if(led_value != null && led_value >= 0 && led_value <= 100){
//                myRef.child("light_value").setValue(led_value)
//            }
//        }
//
//        binding.sensorsetBtn.setOnClickListener {
//            showSensorDialog()
//        }
//        binding.hometap.setOnClickListener{
//            it.findNavController().navigate(R.id.action_sensorFragment_to_homeFragment)
//        }
//        binding.talktap.setOnClickListener{
//            it.findNavController().navigate(R.id.action_sensorFragment_to_talkFragment)
//        }
//        binding.bookmarktap.setOnClickListener{
//            it.findNavController().navigate(R.id.action_sensorFragment_to_bookmarkFragment)
//        }
//        binding.storetap.setOnClickListener{
//            it.findNavController().navigate(R.id.action_sensorFragment_to_storeFragment)
//        }
        return binding.root
    }

    private fun showSensorDialog(){
        val mDialogView = layoutInflater.inflate(R.layout.sensorset_dialog, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("기기코드 등록")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.submitBtn)?.setOnClickListener {
            val sensorCode =  alertDialog.findViewById<EditText>(R.id.sensorCodeArea).text.toString()
            val uid = FBAuth.getUid()

            System.out.println(sensorCode)
            val database : FirebaseDatabase = FirebaseDatabase.getInstance()
            val userRef : DatabaseReference = database.getReference("userData/"+uid)

            var myRef : DatabaseReference = database.getReference("sensor/"+sensorCode)
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

                    val limit_ligth = dataSnapshot.child("limit_light").value
                    val limit_soil = dataSnapshot.child("limit_soil").value
                    val light_value = dataSnapshot.child("light_value").value
                    binding.limitlighttext.text = limit_ligth.toString() + "%"
                    binding.limitsoiltext.text = limit_soil.toString() + "%"
                    binding.ledtext.text = light_value.toString() + "%"
                }
                override fun onCancelled(error: DatabaseError) {  // Failed to read value
                }
            })
            userRef.child("sensorId").setValue(sensorCode)
            alertDialog.dismiss()

        }
        alertDialog.findViewById<Button>(R.id.cancelBtn)?.setOnClickListener {
            alertDialog.dismiss()
        }

    }
//
//
//    private fun getPath(uid : String) : String{
//
//        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
//        val userRef : DatabaseReference = database.getReference("userData/" + uid)
//        System.out.println("checkpoint1 path : " + "userData/" + uid)
//        var sensorCode = ""
//        userRef.child("sensorId").get().addOnSuccessListener{
//            sensorCode = it.value.toString()
//            System.out.println("checkpoint2 path : " + sensorCode)
//            System.out.println("checkpoint3 path : " + it.value.toString())
//        }
//        System.out.println("checkpoint4 path : " + sensorCode)
//
//        return "sensor/"+sensorCode
//
//    }

}