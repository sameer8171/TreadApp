package com.acutepro.treadapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.acutepro.common.SessionPref

object SharedPref {

    fun storeData(
        name:String,
        email:String,
        bio:String,
        userName:String,
        imageUrl:String,
        context: Context
    ){
       val sharedPreferences = context.getSharedPreferences(SessionPref.USERS,MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(SessionPref.NAME,name)
            .putString(SessionPref.EMAIL,email)
            .putString(SessionPref.BIO,bio)
            .putString(SessionPref.USER_NAME,userName)
            .putString(SessionPref.IMAGE_URL,imageUrl).apply()
    }

    fun getName(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(SessionPref.USERS, MODE_PRIVATE)
        return sharedPreferences.getString(SessionPref.NAME,"")!!
    }

    fun getEmail(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(SessionPref.USERS, MODE_PRIVATE)
        return sharedPreferences.getString(SessionPref.EMAIL,"")!!
    }

    fun getBio(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(SessionPref.USERS, MODE_PRIVATE)
        return sharedPreferences.getString(SessionPref.BIO,"")!!
    }

    fun getUserName(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(SessionPref.USERS, MODE_PRIVATE)
        return sharedPreferences.getString(SessionPref.USER_NAME,"")!!
    }

    fun getImageUrl(context: Context):String{
        val sharedPreferences = context.getSharedPreferences(SessionPref.USERS, MODE_PRIVATE)
        return sharedPreferences.getString(SessionPref.IMAGE_URL,"")!!
    }

}