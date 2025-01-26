package com.example.internintelligence_movieapp.ui.me

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.internintelligence_movieapp.R
import com.example.internintelligence_movieapp.base.Resource
import com.example.internintelligence_movieapp.databinding.FragmentMeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MeFragment : Fragment() {
    private lateinit var binding: FragmentMeBinding
    val viewModel:MeViewModel by viewModels()
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        firestore= Firebase.firestore
        buttonLogout()
        viewModel.fetchUserInformation()
        viewModel.userInformation.observe(viewLifecycleOwner) { userResource ->
            when (userResource) {
                is Resource.Success -> {
                    binding.textName.text="Hi, ${userResource.data.username}!"
                   /* Glide.with(binding.root)
                        .load(userResource.data.imageUrl)
                        .into(binding.imgProfile)*/
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), "${userResource.exception}", Toast.LENGTH_SHORT).show()

                }

                is Resource.Loading -> {
                }
            }
        }


    }
    private fun buttonLogout() {
        binding.logOut.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.logout_dialog)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val btnLogout: TextView = dialog.findViewById(R.id.btnLogOut)
            val btnCancel: TextView = dialog.findViewById(R.id.btnCancel)

            btnLogout.setOnClickListener {
//                firestore.collection(ConstValues.USERS).document(auth.currentUser!!.uid)
//                    .update(ConstValues.TOKEN, "")
                auth.signOut()
                findNavController().navigate(R.id.action_meFragment_to_loginFragment)
                dialog.dismiss()
            }
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }


}