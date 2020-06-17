package com.example.imitationqqmusic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.model.bean.SongItem
import kotlinx.android.synthetic.main.current_item.view.*

class CurrentListAdapter(private val listener: OnItemClickListener): ListAdapter<SongItem, CurrentListAdapter.MyHolder>(Compare) {

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    object Compare : DiffUtil.ItemCallback<SongItem>() {
        override fun areItemsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return newItem.songMid == oldItem.songMid
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return newItem === oldItem
        }

    }

    interface OnItemClickListener{
        fun onClick(position: Int, list: List<SongItem>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.current_item, parent, false).also {
            val holder = MyHolder(it)
            holder.itemView.setOnClickListener {
                listener.onClick(holder.absoluteAdapterPosition, currentList)
            }
            return holder
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder.itemView){
            val item = getItem(position)
            tv_text.text = item.name + " - " + item.singer
        }
    }
}