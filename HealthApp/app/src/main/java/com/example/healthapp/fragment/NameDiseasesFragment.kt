package com.example.healthapp.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.Adapter.AdapterDiseases
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentNameDiseasesBinding
import com.example.healthapp.model.BaseDiseases
import com.example.healthapp.model.Diseases
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NameDiseasesFragment : Fragment(), AdapterDiseases.OnItemClickListener1 {
    private var _binding: FragmentNameDiseasesBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<Diseases>()
    private var tvvvv: TextView? = null
    private var arr = ArrayList<String>()
    private var dialog_: Dialog? = null
    private var check = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNameDiseasesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadNameDiseases()
        binding.backFragment.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnNextAddRecordDiseases.setOnClickListener {
            openDialogListDiseases(Gravity.BOTTOM)
        }
    }

    private fun loadNameDiseases() {
        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()
        MySingleton.network.getDiseases(idUser).enqueue(object : Callback<BaseDiseases> {
            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<BaseDiseases>, response: Response<BaseDiseases>) {
                list.clear()
                response.body()?.resultDiseases?.forEach {
                    list.add(it)
                }
                binding.recyclerview.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                binding.recyclerview.adapter =
                    AdapterDiseases(list, requireContext(), this@NameDiseasesFragment)
            }

            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {
            }

        })
    }

    override fun onItemClicked1(position: Int) {
        if (check == 0){
            Toast.makeText(requireContext(), "nothing", Toast.LENGTH_SHORT).show()
        }else{
            val myModel = list[position]
            val url = myModel.url_of_diseases
            val bundle = Bundle()
            bundle.putString("iUrl", url)
            dialog_?.dismiss()
            findNavController().navigate(R.id.listDiseasesFromAlphabetFragment, bundle)
            check = 0
        }
    }

    private fun openDialogListDiseases(gravity: Int) {
        check = 1
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.form_list)

        val window: Window? = dialog.window
        if (window === null) {
            return
        }

        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val windowAttributes: WindowManager.LayoutParams = window.attributes
        windowAttributes.gravity = gravity
        window.attributes = windowAttributes

        val rc: RecyclerView = dialog.findViewById(R.id.recyclerview)
        val searchView: SearchView = dialog.findViewById(R.id.inputSearch1)
        val getArrNameDiseases: TextView = dialog.findViewById(R.id.getNameDiseasesAndMedicine)
        val btnClose = dialog.findViewById<TextView>(R.id.btnCloseDialog)

        tvvvv = getArrNameDiseases
        tvvvv?.text = arr.toString()
        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()

        MySingleton.network.getDiseasesOfWebSite().enqueue(object : Callback<BaseDiseases> {
            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<BaseDiseases>, response: Response<BaseDiseases>) {
                list.clear()
                response.body()?.resultDiseases?.forEach {
                    list.add(it)
                }
                rc.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                rc.adapter =
                    AdapterDiseases(list, dialog.context, this@NameDiseasesFragment)
            }

            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
                if (!TextUtils.isEmpty(query?.trim())) {
                    MySingleton.network.getDiseasesOfWebSite()
                        .enqueue(object : Callback<BaseDiseases> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseDiseases>,
                                response: Response<BaseDiseases>
                            ) {
                                list.clear()
                                response.body()?.resultDiseases?.forEach {
                                    if (query != null) {
                                        if (it.name_diseases.toLowerCase()
                                                .contains(query.toLowerCase())
                                        ) {
                                            list.add(it)
                                        }
                                    }
                                }
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterDiseases(
                                        list,
                                        dialog.context,
                                        this@NameDiseasesFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
                        })
                } else {
                    MySingleton.network.getDiseasesOfWebSite()
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
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterDiseases(
                                        list,
                                        dialog.context,
                                        this@NameDiseasesFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
                        })
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                searchView.clearFocus()
                if (!TextUtils.isEmpty(newText?.trim())) {
                    MySingleton.network.getDiseasesOfWebSite()
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
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterDiseases(
                                        list,
                                        dialog.context,
                                        this@NameDiseasesFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
                        })
                } else {
                    MySingleton.network.getDiseasesOfWebSite()
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
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterDiseases(
                                        list,
                                        dialog.context,
                                        this@NameDiseasesFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
                        })
                }
                return true
            }
        })

        dialog_ = dialog
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

}