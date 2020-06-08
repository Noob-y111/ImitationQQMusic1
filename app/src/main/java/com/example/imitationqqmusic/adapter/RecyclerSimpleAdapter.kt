package com.example.imitationqqmusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imitationqqmusic.R

class RecyclerSimpleAdapter: RecyclerView.Adapter<RecyclerSimpleAdapter.MyHolder>() {
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.music_fragment_item, parent, false).also {
            val holder = MyHolder(it)
            holder.itemView.setOnClickListener {

            }
            return holder
        }
    }

    override fun getItemCount() = 3

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder.itemView) {
            val name: String
            val path: String
            when (position) {
                0 -> {
                    name = "QQMusic新歌榜"
                }

                1 -> {
                    name = "新歌速递"
                }

                2 -> {
                    name = "新碟上架"
                }
            }

        }
    }
}