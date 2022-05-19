package com.example.healthapp.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.Adapter.AdapterDiseases
import com.example.healthapp.R
import com.example.healthapp.activity.DrugActivity
import com.example.healthapp.activity.TotalDrugFromDiseasesActivity
import com.example.healthapp.databinding.FragmentListDiseasesFromAlphabetBinding
import com.example.healthapp.model.BaseDiseases
import com.example.healthapp.model.Diseases
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ListDiseasesFromAlphabetFragment : Fragment(), AdapterDiseases.OnItemClickListener1 {
    private var _binding: FragmentListDiseasesFromAlphabetBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<Diseases>()
    private var url = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListDiseasesFromAlphabetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url = arguments?.getString("iUrl").toString()
        Handler(Looper.getMainLooper()).postDelayed({
            funLoadData(url)
        }, 1000)
        binding.inputSearch2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                binding.inputSearch2.clearFocus()
                if (!TextUtils.isEmpty(query?.trim())) {
                    if (url != null) {
                        MySingleton.network.getListOfDiseasesFromAlphabet(url)
                            .enqueue(object : Callback<BaseDiseases> {
                                @SuppressLint("WrongConstant")
                                override fun onResponse(
                                    call: Call<BaseDiseases>,
                                    response: Response<BaseDiseases>
                                ) {
                                    list.clear()
                                    response.body()?.resultDiseases?.forEach {
                                        if (query != null) if (it.name_diseases.toLowerCase(Locale.ROOT)
                                                .contains(query.toLowerCase(Locale.ROOT))
                                        ) {
                                            list.add(it)
                                        }
                                    }
                                    binding.recyclerview1.layoutManager =
                                        LinearLayoutManager(
                                            requireContext(),
                                            LinearLayout.VERTICAL,
                                            false
                                        )
                                    binding.recyclerview1.adapter =
                                        AdapterDiseases(
                                            list,
                                            requireContext(),
                                            this@ListDiseasesFromAlphabetFragment
                                        )
                                }

                                override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
                            })
                    }
                } else {
                    if (url != null) {
                        MySingleton.network.getListOfDiseasesFromAlphabet(url)
                            .enqueue(object : Callback<BaseDiseases> {
                                @SuppressLint("WrongConstant")
                                override fun onResponse(
                                    call: Call<BaseDiseases>,
                                    response: Response<BaseDiseases>
                                ) {
                                    list.clear()
                                    response.body()?.resultDiseases?.forEach {
                                        list.add(it)
                                    }
                                    binding.recyclerview1.layoutManager =
                                        LinearLayoutManager(
                                            requireContext(),
                                            LinearLayout.VERTICAL,
                                            false
                                        )
                                    binding.recyclerview1.adapter =
                                        AdapterDiseases(
                                            list,
                                            requireContext(),
                                            this@ListDiseasesFromAlphabetFragment
                                        )
                                }

                                override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
                            })
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                binding.inputSearch2.clearFocus()
                if (!TextUtils.isEmpty(newText?.trim())) {
                    if (url != null) {
                        MySingleton.network.getListOfDiseasesFromAlphabet(url)
                            .enqueue(object : Callback<BaseDiseases> {
                                @SuppressLint("WrongConstant")
                                override fun onResponse(
                                    call: Call<BaseDiseases>,
                                    response: Response<BaseDiseases>
                                ) {
                                    list.clear()
                                    response.body()?.resultDiseases?.forEach {
                                        if (newText != null) {
                                            if (it.name_diseases.toLowerCase()
                                                    .contains(newText.toLowerCase())
                                            ) {
                                                list.add(it)
                                            }
                                        }
                                    }
                                    binding.recyclerview1.layoutManager =
                                        LinearLayoutManager(
                                            requireContext(),
                                            LinearLayout.VERTICAL,
                                            false
                                        )
                                    binding.recyclerview1.adapter =
                                        AdapterDiseases(
                                            list,
                                            requireContext(),
                                            this@ListDiseasesFromAlphabetFragment
                                        )
                                }

                                override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
                            })
                    }
                } else {
                    if (url != null) {
                        MySingleton.network.getListOfDiseasesFromAlphabet(url)
                            .enqueue(object : Callback<BaseDiseases> {
                                @SuppressLint("WrongConstant")
                                override fun onResponse(
                                    call: Call<BaseDiseases>,
                                    response: Response<BaseDiseases>
                                ) {
                                    list.clear()
                                    response.body()?.resultDiseases?.forEach {
                                        if (newText != null) {
                                            if (it.name_diseases.toLowerCase()
                                                    .contains(newText.toLowerCase())
                                            ) {
                                                list.add(it)
                                            }
                                        }
                                    }
                                    binding.recyclerview1.layoutManager =
                                        LinearLayoutManager(
                                            requireContext(),
                                            LinearLayout.VERTICAL,
                                            false
                                        )
                                    binding.recyclerview1.adapter =
                                        AdapterDiseases(
                                            list,
                                            requireContext(),
                                            this@ListDiseasesFromAlphabetFragment
                                        )
                                }

                                override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
                            })
                    }
                }
                return false
            }
        })
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.saveDiseases.setOnClickListener {
            val nameDiseases = binding.nameDiseases.text.toString()
            if (nameDiseases == ""){
                Toast.makeText(requireContext(), "Nothing", Toast.LENGTH_SHORT).show()
            }else{
                addNameDiseases(nameDiseases)
            }
        }
    }

    private fun funLoadData(url: String) {
        Log.e("urlOfListDiseases", url)
        MySingleton.network.getListOfDiseasesFromAlphabet(url)
            .enqueue(object : Callback<BaseDiseases> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<BaseDiseases>,
                    response: Response<BaseDiseases>
                ) {
                    list.clear()
                    response.body()?.resultDiseases?.forEach {
                        list.add(it)
                    }
                    binding.recyclerview1.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                    binding.recyclerview1.adapter =
                        AdapterDiseases(
                            list,
                            requireContext(),
                            this@ListDiseasesFromAlphabetFragment
                        )
                }

                override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}

            })
    }

    override fun onItemClicked1(position: Int) {
        val myModel = list[position]
        val nameDiseases = myModel.name_diseases
        binding.nameDiseases.text = nameDiseases
    }

    private fun addNameDiseases(nameDiseases: String) {
        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)

        val idUser = channel.toString().toInt()

        MySingleton.network.addDiseases(idUser, nameDiseases).enqueue(object : Callback<Diseases> {
            override fun onResponse(call: Call<Diseases>, response: Response<Diseases>) {}
            override fun onFailure(call: Call<Diseases>, t: Throwable) {}
        })
        Toast.makeText(requireContext(), "DONE", Toast.LENGTH_SHORT).show()
        binding.nameDiseases.text = ""
        findNavController().navigateUp()
    }
}