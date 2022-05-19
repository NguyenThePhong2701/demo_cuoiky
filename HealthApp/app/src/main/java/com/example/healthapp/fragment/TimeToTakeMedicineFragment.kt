package com.example.healthapp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.Adapter.AdapterTimeToTakeMedicine
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentTimeToTakeMedicineBinding
import com.example.healthapp.model.TimeToTakeMedicine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class TimeToTakeMedicineFragment : Fragment(), AdapterTimeToTakeMedicine.ClickGetTime {
    private var _binding: FragmentTimeToTakeMedicineBinding? = null
    private val binding get() = _binding!!
    private var list = ArrayList<TimeToTakeMedicine>()
    private var myAdapter: AdapterTimeToTakeMedicine? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeToTakeMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        load()
        loading()
        buildRecyclerView()
        Log.d("arr", list.toString())
        binding.backFragment.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAddTime.setOnClickListener {
            findNavController().navigate(R.id.addTimeToTakeMedicineFragment)
        }

        binding.backFragment.setOnClickListener {
            val sharedPref: SharedPreferences =
                requireContext().getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()
        }

    }

    override fun onItemClicked(position: Int) {
        val myModel = list[position]
        val dataBundle = Bundle()

        val gNameMedicine = myModel.name_diseases
        val gHour = myModel.saveHour
        val gMinutes = myModel.saveMinutes
        val gTitleAlarm = myModel.titleAlarm
        val gNumberDrug = myModel.number_drug
        val gStatus = myModel.status

        dataBundle.putString("iNameMedicine", gNameMedicine)
        dataBundle.putString("iPosition", position.toString())
        dataBundle.putString("iHour", gHour)
        dataBundle.putString("iMinutes", gMinutes)
        dataBundle.putString("iTitleAlarm", gTitleAlarm)
        dataBundle.putString("iNumberDrug", gNumberDrug)
        dataBundle.putString("iStatus", gStatus)

        findNavController().navigate(R.id.detailAlarmTimeFragment, dataBundle)
        Toast.makeText(requireContext(), "$position///$gHour : $gMinutes///$gTitleAlarm///$gStatus", Toast.LENGTH_SHORT).show()
    }

    private fun load() {
        val gson = Gson()
        val mPrefs: SharedPreferences = requireContext().getSharedPreferences(
            "shared preferences",
            Context.MODE_PRIVATE
        )
        val json1 = mPrefs.getString("tag1", "null")
        val type: Type = object : TypeToken<ArrayList<TimeToTakeMedicine?>?>() {}.type
        val data: ArrayList<TimeToTakeMedicine> = Gson().fromJson(json1, type)
        data.forEach {
            list.add(it)
        }
        Log.d("arr", list.toString())
    }

    private fun loading(){
        val shared = activity?.getSharedPreferences("cookie", AppCompatActivity.MODE_PRIVATE)
        val channel = shared?.getString("idOfUser", null)
        val idUser = channel.toString().toInt()
        val check = getDataInArrayList("tag1${idUser.toString()}")
        val jsonTime = getDataInArrayList("tag1${idUser.toString()}")
        if (check == null){
            jsonTime.forEach {
                list.add(it)
            }
        }else{
            list.clear()
            jsonTime.forEach {
                list.add(it)
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun buildRecyclerView() {
        myAdapter =
            AdapterTimeToTakeMedicine(list, requireContext(), this)
        val recyclerView: RecyclerView = binding.recyclerview
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
        recyclerView.adapter = myAdapter
    }

    private fun getDataInArrayList(key: String): ArrayList<TimeToTakeMedicine> {
        val mPrefs: SharedPreferences = requireContext().getSharedPreferences(
            "shared preferences",
            Context.MODE_PRIVATE
        )
        val emptyList = Gson().toJson(ArrayList<TimeToTakeMedicine>())
        return Gson().fromJson(
            mPrefs.getString(key, emptyList),
            object : TypeToken<ArrayList<TimeToTakeMedicine>>() {
            }.type
        )
    }


}