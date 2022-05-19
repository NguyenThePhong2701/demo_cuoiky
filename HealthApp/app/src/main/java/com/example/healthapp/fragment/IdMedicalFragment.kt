package com.example.healthapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentIdMedicalBinding
import com.example.healthapp.model.BaseNewRecord
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IdMedicalFragment : Fragment() {
    private var _binding: FragmentIdMedicalBinding? = null
    private val binding get() = _binding!!
    private var idUser = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentIdMedicalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        idUser = channel.toString().toInt()

        binding.btnEdit.setOnClickListener {
            findNavController().navigate(R.id.editIdMedicalFragment)
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        MySingleton.network.getDataNewRecord(idUser).enqueue(object : Callback<BaseNewRecord> {
            override fun onResponse(call: Call<BaseNewRecord>, response: Response<BaseNewRecord>) {
                response.body()?.resultNewRecord?.forEach {
                    binding.labelName.text = it.name
                    binding.labelBirthDay.text = it.birth_day
                    binding.labelHeight.text = it.height + " cm"
                    binding.labelWeight.text = it.height + " kg"
                }
            }

            override fun onFailure(call: Call<BaseNewRecord>, t: Throwable) {}

        })
    }
}