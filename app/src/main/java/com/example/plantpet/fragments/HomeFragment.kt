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