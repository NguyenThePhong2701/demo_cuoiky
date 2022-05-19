package com.example.healthapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.Adapter.AdapterDetailMedicine
import com.example.healthapp.databinding.FragmentDetailRecordMedicalBinding
import com.example.healthapp.model.BaseDetailMedicine
import com.example.healthapp.model.DetailMedicine
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailRecordMedicalFragment : Fragment(), AdapterDetailMedicine.OnItemClickListener4 {
    private var _binding: FragmentDetailRecordMedicalBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<DetailMedicine>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailRecordMedicalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()

        val idRecord = arguments?.getString("iIdRecord")
        binding.labelDate.text = arguments?.getString("iDate")
        binding.labelDiseases.text = arguments?.getString("iDiseases")
        binding.labelMedicine.text = arguments?.getString("iMedicine")
        binding.tvNameDoctor.text = arguments?.getString("iNameDoctor")
        binding.tvNumberPhone.text = arguments?.getString("iNumberPhone")

        if (idRecord != null) {
            loadData(idUser, idRecord.toInt())
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        Log.e("idUser", idUser.toString())
        Log.e("idRecord", idRecord.toString())
    }


    private fun loadData(idUser: Int, idRecord: Int) {
        MySingleton.network.getDetailMedicine(idUser, idRecord)
            .enqueue(object : Callback<BaseDetailMedicine> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<BaseDetailMedicine>,
                    response: Response<BaseDetailMedicine>
                ) {
                    response.body()?.resultDetailMedicine?.forEach {
                        list.add(it)
                        binding.recyclerview.layoutManager =
                            LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                        binding.recyclerview.adapter = AdapterDetailMedicine(
                            list,
                            requireContext(),
                            this@DetailRecordMedicalFragment
                        )
                    }
                }
                override fun onFailure(call: Call<BaseDetailMedicine>, t: Throwable) {}
            })
    }

    override fun onItemClicked4(position: Int) {
    }

}