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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.Adapter.AdapterDiseases
import com.example.healthapp.Adapter.AdapterMedicine
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentNameMedicineBinding
import com.example.healthapp.model.BaseDiseases
import com.example.healthapp.model.BaseMedicine
import com.example.healthapp.model.Medicine
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NameMedicineFragment : Fragment() , AdapterMedicine.OnItemClickListener2{
    private var _binding: FragmentNameMedicineBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<Medicine>()
    private var dialog_: Dialog? = null
    private var check = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentNameMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()
        loadNameMedicine(idUser)

        binding.backFragment.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnNextAddRecordMedicine.setOnClickListener {
//            findNavController().navigate(R.id.addMedicineFragment)
            openDialogListDiseases(Gravity.BOTTOM)
        }

    }
    override fun onItemClicked2(position: Int) {
        if (check == 0){
            Toast.makeText(requireContext(), "nothing", Toast.LENGTH_SHORT).show()
        }else{
            val myModel = list[position]
            val url = myModel.url_drug_of_diseases
            val bundle = Bundle()
            bundle.putString("iUrl", url)
            dialog_?.dismiss()
            findNavController().navigate(R.id.addMedicineFragment, bundle)
            check = 0
        }
    }

    private fun loadNameMedicine(idUser: Int){
        MySingleton.network.getMedicine(idUser).enqueue(object : Callback<BaseMedicine>{
            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<BaseMedicine>, response: Response<BaseMedicine>) {
                list.clear()
                response.body()?.resultMedicine?.forEach {
                    list.add(it)
                }
                binding.recyclerview.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                binding.recyclerview.adapter =
                    AdapterMedicine(list, requireContext(), this@NameMedicineFragment)
            }
            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
        })
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
        val btnClose = dialog.findViewById<TextView>(R.id.btnCloseDialog)


        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()

        MySingleton.network.getMedicineOfWebSite().enqueue(object : Callback<BaseMedicine> {
            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<BaseMedicine>, response: Response<BaseMedicine>) {
                list.clear()
                response.body()?.resultMedicine?.forEach {
                    list.add(it)
                }
                rc.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                rc.adapter =
                    AdapterMedicine(list, dialog.context, this@NameMedicineFragment)
            }

            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
                if (!TextUtils.isEmpty(query?.trim())) {
                    MySingleton.network.getMedicineOfWebSite()
                        .enqueue(object : Callback<BaseMedicine> {
                            @SuppressLint("WrongConstant")
                            override fun onResponse(
                                call: Call<BaseMedicine>,
                                response: Response<BaseMedicine>
                            ) {
                                list.clear()
                                response.body()?.resultMedicine?.forEach {
                                    if (query != null) {
                                        if (it.name_medicine.toLowerCase()
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
                                    AdapterMedicine(
                                        list,
                                        dialog.context,
                                        this@NameMedicineFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
                        })
                } else {
                    MySingleton.network.getMedicineOfWebSite()
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
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterMedicine(
                                        list,
                                        dialog.context,
                                        this@NameMedicineFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
                        })
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                searchView.clearFocus()
                if (!TextUtils.isEmpty(newText?.trim())) {
                    MySingleton.network.getMedicineOfWebSite()
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
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterMedicine(
                                        list,
                                        dialog.context,
                                        this@NameMedicineFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
                        })
                } else {
                    MySingleton.network.getMedicineOfWebSite()
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
                                rc.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayout.VERTICAL,
                                        false
                                    )
                                rc.adapter =
                                    AdapterMedicine(
                                        list,
                                        dialog.context,
                                        this@NameMedicineFragment
                                    )
                            }

                            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
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