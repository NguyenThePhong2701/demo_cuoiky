package com.example.healthapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.healthapp.R
import com.example.healthapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        
        setupWithNavController(binding.bottomNav, navController)

        navController.addOnDestinationChangedListener { controller, _, _ ->
            binding.bottomNav.isVisible = when (controller.currentDestination?.id) {
                R.id.item_activity_browser, R.id.item_activity_overall_heath -> true
                else -> false
            }
        }

    }
}