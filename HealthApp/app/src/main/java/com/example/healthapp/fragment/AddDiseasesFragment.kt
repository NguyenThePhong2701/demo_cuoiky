package com.example.healthapp.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.Adapter.AdapterDiseases
import com.example.healthapp.R
import com.example.healthapp.databinding.FragmentAddDiseasesBinding
import com.example.healthapp.model.BaseDiseases
import com.example.healthapp.model.Diseases
import com.example.healthapp.mySingleton.MySingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddDiseasesFragment : Fragment(), AdapterDiseases.OnItemClickListener1 {
    private var _binding: FragmentAddDiseasesBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<Diseases>()
    private val list1 = mutableListOf<Diseases>()

    private var tvvvv: TextView? = null
    private var arr = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDiseasesBinding.inflate(inflater, container, false)
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
                    AdapterDiseases(list, requireContext(), this@AddDiseasesFragment)
            }

            override fun onFailure(call: Call<BaseDiseases>, t: Throwable) {}
        })
    }

    override fun onItemClicked1(position: Int) {
        val myModel = list[position]
        val url = myModel.url_of_diseases
        val bundle = Bundle()
        bundle.putString("iUrl", url)
//        findNavController().navigate(R.id.listDiseasesFromAlphabetFragment, bundle)
    }


}