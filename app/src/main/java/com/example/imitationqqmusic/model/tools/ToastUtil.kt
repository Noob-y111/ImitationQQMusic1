package com.example.imitationqqmusic.model.tools

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

class ToastUtil {

    companion object{
        private var toast: Toast? = null

        @SuppressLint("ShowToast")
        fun showToast(context: Context, message: String){
            if (toast == null){
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            }else{
                toast!!.setText(message)
                toast!!.duration = Toast.LENGTH_SHORT
            }
            toast!!.show()
        }
    }

}