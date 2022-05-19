package com.example.healthapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthapp.Adapter.AdapterMedicine
import com.example.healthapp.databinding.ActivityTotalDrugFromDiseasesBinding
import com.example.healthapp.model.BaseMedicine
import com.example.healthapp.model.Medicine
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TotalDrugFromDiseasesActivity : AppCompatActivity(), AdapterMedicine.OnItemClickListener2 {
    private var _binding: ActivityTotalDrugFromDiseasesBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<Medicine>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityTotalDrugFromDiseasesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val url = intent.getStringExtra("iUrl").toString()
        Handler(Looper.getMainLooper()).postDelayed({
            loadData(url)
        }, 2000)
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
                        this@TotalDrugFromDiseasesActivity,
                        LinearLayout.VERTICAL,
                        false
                    )
                binding.recyclerview.adapter =
                    AdapterMedicine(
                        list,
                        this@TotalDrugFromDiseasesActivity,
                        this@TotalDrugFromDiseasesActivity
                    )
            }

            override fun onFailure(call: Call<BaseMedicine>, t: Throwable) {}
        })
    }

    override fun onItemClicked2(position: Int) {
        Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show()
    }
}