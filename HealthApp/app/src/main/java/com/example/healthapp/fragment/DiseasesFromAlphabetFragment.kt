package com.example.healthapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.Adapter.AdapterDiseases
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentDrugFromDiseasesBinding
import com.example.healthapp.model.BaseDiseases
import com.example.healthapp.model.Diseases
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DiseasesFromAlphabetFragment : Fragment(), AdapterDiseases.OnItemClickListener1 {
    private var _binding: FragmentDrugFromDiseasesBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<Diseases>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrugFromDiseasesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            loadData()
        }, 1000)
    }

    private fun loadData() {
        list.clear()
        MySingleton.network.getDiseasesOfWebSite().enqueue(object : Callback<BaseDiseases> {
            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<BaseDiseases>, response: Response<BaseDiseases>) {
                response.body()?.resultDiseases?.forEach {
                    list.add(it)
                }
                binding.recyclerview.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                binding.recyclerview.adapter =
                    AdapterDiseases(list, requireContext(), this@DiseasesFromAlphabetFragment)
            }

            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
        })
    }

    override fun onItemClicked1(position: Int) {
        val myModel = list[position]
        val url = myModel.url_of_diseases
        MySingleton.network.getListOfDiseasesFromAlphabet(url)
            .enqueue(object : Callback<BaseDiseases> {
                override fun onResponse(
                    call: Call<BaseDiseases>,
                    response: Response<BaseDiseases>
                ) {
                }

                override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
            })
        val bundle = Bundle()
        bundle.putString("iUrl", url)
        findNavController().navigate(R.id.listDiseasesFromAlphabetFragment, bundle)
    }
}