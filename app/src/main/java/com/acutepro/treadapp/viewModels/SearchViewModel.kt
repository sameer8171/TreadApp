package com.acutepro.treadapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acutepro.treadapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val users = db.getReference("users")



    private var _Users = MutableLiveData<List<UserModel>>()
    val Users: LiveData<List<UserModel>> = _Users

    init {
        fetchUsers {
            _Users.value = it
        }
    }



    private fun fetchUsers(onResult: (List<UserModel>) -> Unit){
        users.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<UserModel>()
                for (threadSnapshot in snapshot.children){
                    val userModel = threadSnapshot.getValue(UserModel::class.java)
                    if (!userModel!!.uid.contains(FirebaseAuth.getInstance().currentUser!!.uid)) {
                        result.add(userModel!!)
                    }
                }
                onResult(result)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }









}