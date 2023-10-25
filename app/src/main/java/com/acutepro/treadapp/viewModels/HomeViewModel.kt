package com.acutepro.treadapp.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acutepro.treadapp.model.AddThreadsModel
import com.acutepro.treadapp.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class HomeViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val thread = db.getReference("threads")



    private var _threadsAndUsers = MutableLiveData<List<Pair<AddThreadsModel,UserModel>>>()
    val threadsAndUsers: LiveData<List<Pair<AddThreadsModel,UserModel>>> = _threadsAndUsers

    init {
        fetchThreadAndUsers {
            _threadsAndUsers.value = it
        }
    }



    private fun fetchThreadAndUsers(onResult: (List<Pair<AddThreadsModel,UserModel>>) -> Unit){
        thread.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Pair<AddThreadsModel,UserModel>>()
                for (threadSnapshot in snapshot.children){
                    val thread = threadSnapshot.getValue(AddThreadsModel::class.java)
                    thread.let {
                        fetchUserFromThread(it!!){user ->
                            result.add(0,it to user)
                            if (result.size == snapshot.childrenCount.toInt()){
                                onResult(result)
                            }

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun fetchUserFromThread(thread:AddThreadsModel , onResult:(UserModel) -> Unit){
        db.getReference("users").child(thread.userId)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }







}