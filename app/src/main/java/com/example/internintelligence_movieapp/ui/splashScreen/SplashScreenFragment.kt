package com.example.internintelligence_movieapp.ui.splashScreen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.internintelligence_movieapp.MainActivity
import com.example.internintelligence_movieapp.R

class SplashScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)

        (activity as? MainActivity)?.hideBottomNavigationView()

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded) {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
            }, 1000)
        }
    }

}