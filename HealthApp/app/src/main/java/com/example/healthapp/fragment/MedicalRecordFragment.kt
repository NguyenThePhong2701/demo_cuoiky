package com.example.healthapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.Adapter.AdapterBrowserFragment
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentMedicalRecordBinding
import com.example.healthapp.model.BaseRecordMedical
import com.example.healthapp.model.RecordMedical
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicalRecordFragment : Fragment(), AdapterBrowserFragment.OnItemClickListener {
    private var _binding: FragmentMedicalRecordBinding? = null
    private val binding get() = _binding!!
    private var list = mutableListOf<RecordMedical>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicalRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddMedicalRecord.setOnClickListener {
            findNavController().navigate(R.id.addRecordMedicalFragment)
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        loadData()
    }

    override fun onItemClicked(position: Int) {
        val myModel = list[position]
        val dataBundle = Bundle()

        val gIdRecord: Int = myModel.id
        val gDate: String = myModel.date
        val gTime: String = myModel.time
        val gDiseases: String = myModel.diseases
        val gMedicine: String = myModel.medicine
        val gNameDoctor: String = myModel.name_doctor
        val gNumberPhone: String = myModel.number_phone_of_doctor

        dataBundle.putString("iIdRecord", gIdRecord.toString())
        dataBundle.putString("iDate", gDate)
        dataBundle.putString("iDiseases", gDiseases)
        dataBundle.putString("iMedicine", gMedicine)
        dataBundle.putString("iNameDoctor", gNameDoctor)
        dataBundle.putString("iNumberPhone", gNumberPhone)

        findNavController().navigate(R.id.detailRecordMedical, dataBundle)
        Toast.makeText(requireContext(), gDate, Toast.LENGTH_SHORT).show()
    }

    private fun loadData() {
        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()
        MySingleton.network.getRecordMedical(idUser).enqueue(object : Callback<BaseRecordMedical> {
            @SuppressLint("WrongConstant")
            override fun onResponse(
                call: Call<BaseRecordMedical>,
                response: Response<BaseRecordMedical>
            ) {
                list.clear()
                response.body()?.resultMedical?.forEach {
                    list.add(it)
                }
                binding.recyclerview.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                binding.recyclerview.adapter =
                    AdapterBrowserFragment(list, requireContext(), this@MedicalRecordFragment)
            }
            override fun onFailure(call: Call<BaseRecordMedical>, t: Throwable) {}
        })


    }
}


