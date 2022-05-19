package com.example.healthapp.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.Adapter.AdapterMedicine
import com.example.healthapp.databinding.FragmentListDrugsFromAlphabetBinding
import com.example.healthapp.model.BaseMedicine
import com.example.healthapp.model.Medicine
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ListDrugsFromAlphabetFragment : Fragment(), AdapterMedicine.OnItemClickListener2 {
    private var _binding: FragmentListDrugsFromAlphabetBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<Medicine>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListDrugsFromAlphabetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val  url = arguments?.getString("iUrl").toString()
        funLoadData(url)
        binding.btnSave.setOnClickListener {
            val namMedicine = binding.inputNameMedicine.text.toString()
            if (namMedicine == ""){
                Toast.makeText(requireContext(), "nothing", Toast.LENGTH_SHORT).show()
            }else{
                addNameMedicine(namMedicine)
            }
        }
        binding.inputSearch2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                binding.inputSearch2.clearFocus()
                if (!TextUtils.isEmpty(query?.trim())) {
                    if (url != null) {
                        MySingleton.network.getListOfDrugsFromAlphabet(url)
                            .enqueue(object : Callback<BaseMedicine> {
                                @SuppressLint("WrongConstant")
                                override fun onResponse(
                                    call: Call<BaseMedicine>,
                                    response: Response<BaseMedicine>
                                ) {
                                    list.clear()
                                    response.body()?.resultMedicine?.forEach {
                                        if (query != null) if (it.name_medicine.toLowerCase(Locale.ROOT)
                                                .contains(query.toLowerCase(Locale.ROOT))
                                        ) {
                                            list.add(it)
                                        }
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
                                            this@ListDrugsFromAlphabetFragment
                                        )
                                }

                                override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
                            })
                    }
                } else {
                    if (url != null) {
                        MySingleton.network.getListOfDrugsFromAlphabet(url)
                            .enqueue(object : Callback<BaseMedicine> {
                                @SuppressLint("WrongConstant")
                                override fun onResponse(
                                    call: Call<BaseMedicine>,
                                    response: Response<BaseMedicine>
                                ) {
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
                                            this@ListDrugsFromAlphabetFragment
                                        )
                                }

                                override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
                            })
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                binding.inputSearch2.clearFocus()
                if (!TextUtils.isEmpty(newText?.trim())) {
                    if (url != null) {
                        MySingleton.network.getListOfDrugsFromAlphabet(url)
                            .enqueue(object : Callback<BaseMedicine> {
                                @SuppressLint("WrongConstant")
                                override fun onResponse(
                                    call: Call<BaseMedicine>,
                                    response: Response<BaseMedicine>
                                ) {
                                    list.clear()
                                    response.body()?.resultMedicine?.forEach {
                                        if (newText != null) {
                                            if (it.name_medicine.toLowerCase()
                                                    .contains(newText.toLowerCase())
                                            ) {
                                                list.add(it)
                                            }
                                        }
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
                                            this@ListDrugsFromAlphabetFragment
                                        )
                                }

                                override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
                            })
                    }
                } else {
                    if (url != null) {
                        MySingleton.network.getListOfDrugsFromAlphabet(url)
                            .enqueue(object : Callback<BaseMedicine> {
                                @SuppressLint("WrongConstant")
                                override fun onResponse(
                                    call: Call<BaseMedicine>,
                                    response: Response<BaseMedicine>
                                ) {
                                    list.clear()
                                    response.body()?.resultMedicine?.forEach {
                                        if (newText != null) {
                                            if (it.name_medicine.toLowerCase()
                                                    .contains(newText.toLowerCase())
                                            ) {
                                                list.add(it)
                                            }
                                        }
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
                                            this@ListDrugsFromAlphabetFragment
                                        )
                                }

                                override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
                            })
                    }
                }
                return false
            }
        })

    }

    private fun addNameMedicine(nameMedicine: String) {
        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()
        MySingleton.network.addMedicine(idUser, nameMedicine)
            .enqueue(object : Callback<Medicine> {
                override fun onResponse(call: Call<Medicine>, response: Response<Medicine>) {}
                override fun onFailure(call: Call<Medicine>, t: Throwable) {}
            })
        Toast.makeText(requireContext(), "DONE", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }


    override fun onItemClicked2(position: Int) {
        val myModel = list[position]
        val nameMedicine = myModel.name_medicine
        binding.inputNameMedicine.text = nameMedicine
    }

    private fun funLoadData(url: String) {
        Log.e("urlOfListDiseases", url)
        MySingleton.network.getListOfDrugsFromAlphabet(url)
            .enqueue(object : Callback<BaseMedicine> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<BaseMedicine>,
                    response: Response<BaseMedicine>
                ) {
                    list.clear()
                    response.body()?.resultMedicine?.forEach {
                        list.add(it)
                    }
                    binding.recyclerview.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                    binding.recyclerview.adapter =
                        AdapterMedicine(
                            list,
                            requireContext(),
                            this@ListDrugsFromAlphabetFragment
                        )
                }
                override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
            })
    }

}