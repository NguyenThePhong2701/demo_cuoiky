package com.example.healthapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.healthapp.R
import com.example.healthapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment1) as NavHostFragment).navController
    }
}