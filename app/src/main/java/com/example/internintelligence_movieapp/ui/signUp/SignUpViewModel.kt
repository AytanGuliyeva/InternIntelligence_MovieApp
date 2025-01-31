package com.example.internintelligence_movieapp.ui.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.internintelligence_movieapp.base.ConstValues
import com.example.internintelligence_movieapp.base.Resource
import com.example.internintelligence_movieapp.data.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val firebaseFirestore: FirebaseFirestore,
    val firebaseAuth: FirebaseAuth
) : ViewModel() {
    // private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    //private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _userCreated = MutableLiveData<Resource<Users>>()
    val userCreated: LiveData<Resource<Users>>
        get() = _userCreated

    fun signUp(username: String, email: String, password: String, phone: String) {
        _userCreated.postValue(Resource.Loading)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                userAccount(username, email, password, phone)
            }
            .addOnFailureListener { exception ->
                _userCreated.postValue(Resource.Error(exception))
            }
    }

    private fun userAccount(username: String, email: String, password: String, phone: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val userMap = hashMapOf(
            ConstValues.USER_ID to userId,
            ConstValues.USERNAME to username,
            ConstValues.EMAIL to email,
            ConstValues.PASSWORD to password,
            ConstValues.PHONE to phone
            //  ConstValues.IMAGE_URL to "https://firebasestorage.googleapis.com/v0/b/globetrotter-1f997.appspot.com/o/profile_photo_default.jpg?alt=media&token=abf8c436-e537-4120-b10d-cace9095f47c"
        )
        val refdb = firebaseFirestore.collection(ConstValues.USERS).document(userId)
        refdb.set(userMap)
            .addOnSuccessListener {
                _userCreated.value =
                    Resource.Success(Users(userId, username, email, password, "", "", phone))
            }
            .addOnFailureListener { exception ->
                _userCreated.postValue((Resource.Error(exception)))
            }
    }

}