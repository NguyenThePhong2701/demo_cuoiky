package com.example.healthapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthapp.Adapter.AdapterBrowserFragment
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentOverallHealthBinding



class OverallHealthFragment : Fragment() {
    private var _binding: FragmentOverallHealthBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverallHealthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // code everything
        binding.btnBottomSheetProfile.setOnClickListener {
            findNavController().navigate(R.id.profileDialogFragment)
        }
        binding.btnStartIdMedical.setOnClickListener {
            findNavController().navigate(R.id.idMedicalFragment)
        }
        binding.btnMedicalRecord.setOnClickListener {
            findNavController().navigate(R.id.medicalRecordFragment)
        }

        binding.testRetrofitWeb.setOnClickListener {

        }
    }
}

