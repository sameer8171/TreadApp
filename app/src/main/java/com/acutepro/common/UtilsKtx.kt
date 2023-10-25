package com.acutepro.common

import android.content.Context
import android.widget.Toast

/** Created by SamCoder 07 Sep 2023
 *
 */

/** Show Toast on Activity
 *
 */
fun Context.showToast(message:String?,length:Int = Toast.LENGTH_SHORT){
    Toast.makeText(this,message?:"Something went wrong!",length).show()
}