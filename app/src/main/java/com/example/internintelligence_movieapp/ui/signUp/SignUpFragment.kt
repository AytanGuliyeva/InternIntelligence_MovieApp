package com.example.internintelligence_movieapp.ui.signUp

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.internintelligence_movieapp.R
import com.example.internintelligence_movieapp.base.Resource
import com.example.internintelligence_movieapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        initNavigationListeners()
    }

    private fun setupViews() {
        binding.buttonSignUp.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            val email = binding.editEmail.text.toString().trim()
            val phone = binding.editPhoneNumber.text.toString().trim()


            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(
                    email
                ) || TextUtils.isEmpty(phone)
            ) {
                Toast.makeText(
                    requireContext(),
                    "All fields are required", Toast.LENGTH_SHORT
                )
                    .show()
            } else if (password.length < 6) {
                Toast.makeText(
                    requireContext(),
                    "Password must have at least 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.signUp(username, email, password, phone)
            }
        }
    }


    private fun observeViewModel() {
        viewModel.userCreated.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    //   binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    // binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.loginFragment)
                }

                is Resource.Error -> {
                    //binding.progressBar.visibility = View.GONE
                    handleSignUpFailure(resource.exception)
                }
            }
        }
    }

    private fun handleSignUpFailure(exception: Throwable) {
        when (exception) {
            is FirebaseAuthUserCollisionException -> {
                Toast.makeText(
                    requireContext(),
                    "User with this email already exist",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                Toast.makeText(
                    requireContext(),
                    "Sign up failed: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initNavigationListeners() {
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
        binding.textLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }


}