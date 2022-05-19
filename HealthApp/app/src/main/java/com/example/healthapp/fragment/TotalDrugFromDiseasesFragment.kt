package com.example.healthapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.Adapter.AdapterDiseases
import com.example.healthapp.Adapter.AdapterMedicine
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentTotalDrugFromDiseasesBinding
import com.example.healthapp.model.BaseMedicine
import com.example.healthapp.model.Medicine
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TotalDrugFromDiseasesFragment : Fragment(),AdapterMedicine.OnItemClickListener2 {
    private var _binding: FragmentTotalDrugFromDiseasesBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<Medicine>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTotalDrugFromDiseasesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = activity?.intent?.getStringExtra("iUrlDrug").toString()
        Log.e("arrr", url.toString())
        if (url != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                loadData(url)
                }, 2000)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadData(url: String) {
        MySingleton.network.getTotalDrugFromDiseases(url).enqueue(object : Callback<BaseMedicine> {
            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<BaseMedicine>, response: Response<BaseMedicine>) {
                list.clear()
                response.body()?.resultMedicine?.forEach {
                    list.add(it)
                }
                binding.recyclerview.layoutManager =
                    LinearLayoutManager(
                        requireContext(),
                        LinearLayout.VERTICAL,
                        false
                    )
                binding.recyclerview.adapter =
                    AdapterMedicine(
                        list,
                        requireContext(),
                        this@TotalDrugFromDiseasesFragment
                    )
            }

            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
        })
    }

    override fun onItemClicked2(position: Int) {
        val myMode = list[position]
        val url = myMode.url_drug_of_diseases
        val bundle = Bundle()
        bundle.putString("iUrl", url)
        findNavController().navigate(R.id.getWebsiteFragment, bundle)
    }
}