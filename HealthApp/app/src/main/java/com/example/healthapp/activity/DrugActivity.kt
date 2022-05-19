package com.example.healthapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.healthapp.R
import com.example.healthapp.databinding.ActivityDrugBinding

class DrugActivity : AppCompatActivity() {
    private var _binding: ActivityDrugBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityDrugBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment2) as NavHostFragment).navController
    }
}