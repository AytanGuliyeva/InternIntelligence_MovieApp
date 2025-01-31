package com.example.internintelligence_movieapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.internintelligence_movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment || destination.id == R.id.searchFragment || destination.id == R.id.savedFragment || destination.id == R.id.downloadsFragment || destination.id == R.id.meFragment) {
                showBottomNavigationView()
            } else {
                hideBottomNavigationView()
            }
        }

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    navController.popBackStack(R.id.homeFragment, false)
                    true
                }

                R.id.searchFragment -> {
                    navController.popBackStack(R.id.searchFragment, false)
                    navController.navigate(R.id.searchFragment)
                    true
                }

                R.id.savedFragment -> {
                    navController.popBackStack(R.id.savedFragment, false)
                    navController.navigate(R.id.savedFragment)
                    true
                }

                R.id.downloadsFragment -> {
                    navController.popBackStack(R.id.downloadsFragment, false)
                    navController.navigate(R.id.downloadsFragment)
                    true
                }
                R.id.meFragment -> {
                    navController.popBackStack(R.id.meFragment, false)
                    navController.navigate(R.id.meFragment)
                    true
                }

                else -> false
            }
        }


    }

    fun hideBottomNavigationView() {
        binding.bottomNav.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        binding.bottomNav.visibility = View.VISIBLE
    }
}