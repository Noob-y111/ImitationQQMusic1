package com.example.imitationqqmusic.model

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleyInstance private constructor(context: Context) {

    companion object {
        private var volley: VolleyInstance? = null

        fun getInstance(context: Context) = volley ?: synchronized(this) {
            VolleyInstance(context).also {
                volley = it
            }
        }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}