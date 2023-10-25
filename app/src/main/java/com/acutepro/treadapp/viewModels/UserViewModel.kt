package com.acutepro.treadapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acutepro.treadapp.model.AddThreadsModel
import com.acutepro.treadapp.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel:ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    private val fireStoreDb = Firebase.firestore

    val threadRef = db.getReference("threads")
    val userRef = db.getReference("users")

    private val _threads= MutableLiveData(listOf<AddThreadsModel>())
    val threads:LiveData<List<AddThreadsModel>> get() = _threads

    private val _users = MutableLiveData(UserModel())
    val users:LiveData<UserModel> get() = _users

    private val _followerList = MutableLiveData(listOf<String>())
    val followerList:LiveData<List<String>> get() = _followerList

    private val _followingList = MutableLiveData(listOf<String>())
    val followingList:LiveData<List<String>> get() = _followingList

    fun fetchUser(uid:String){
        userRef.child(uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user= snapshot.getValue(UserModel::class.java)
                _users.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun fetchThreads(uid: String){
        threadRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val threadList = snapshot.children.mapNotNull {
                    it.getValue(AddThreadsModel::class.java)
                }
                _threads.postValue(threadList)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun followUsers(userId:String,currentUserId:String){
        val ref = fireStoreDb.collection("following").document(currentUserId)
        val followRef = fireStoreDb.collection("followers").document(userId)

        ref.update("followingIds",FieldValue.arrayUnion(userId))
        followRef.update("followerIds",FieldValue.arrayUnion(currentUserId))


    }

    fun getFollower(userId: String){
           fireStoreDb.collection("followers").document(userId)
               .addSnapshotListener { value, error ->
                   val followerIds = value?.get("followerIds") as? List<String> ?: listOf()
                   _followerList.postValue(followerIds)
               }
    }

    fun getFollowing(userId: String){

        fireStoreDb.collection("following").document(userId)
            .addSnapshotListener { value, error ->
                val followerIds = value?.get("followingIds") as? List<String> ?: listOf()
                _followingList.postValue(followerIds)
            }

    }

    fun unFollow(userId: String,currentUserId: String){
        val ref = fireStoreDb.collection("following").document(currentUserId)
        val followRef = fireStoreDb.collection("followers").document(userId)

        ref.update("followingIds",FieldValue.delete())
        followRef.update("followerIds",FieldValue.delete())


    }



}