package com.acutepro.treadapp.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acutepro.treadapp.model.AddThreadsModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AddThreadViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("threads")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("threads/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    private var _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted








     fun saveImage(
        thread: String,
        userId: String,
        imageUri:Uri,
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnCompleteListener {
            imageRef.downloadUrl.addOnSuccessListener {url ->
                saveData(thread = thread,userId = userId,imageUri=url.toString())
            }
        }

    }

     fun saveData(
        thread: String,
        userId: String,
        imageUri: String,
    ) {

        val threadData = AddThreadsModel(thread =  thread, userId =  userId, image = imageUri, timeStamp = System.currentTimeMillis().toString())

        userRef.child(userRef.push().key!!).setValue(threadData)
            .addOnCompleteListener {
               _isPosted.postValue(true)
            }.addOnFailureListener {
                _isPosted.postValue(false)
            }

    }


}