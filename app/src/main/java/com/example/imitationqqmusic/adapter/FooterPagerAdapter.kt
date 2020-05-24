package com.example.imitationqqmusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.model.bean.SongItem
import kotlinx.android.synthetic.main.footer_item.view.*

class FooterPagerAdapter(private val listener: OnItemClick): ListAdapter<SongItem, FooterPagerAdapter.MyHolder>(Compare) {

    object Compare: DiffUtil.ItemCallback<SongItem>() {
        override fun areItemsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return newItem.name == oldItem.name
        }

        override fun areContentsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return newItem.name == oldItem.singer
        }
    }

    interface OnItemClick{
        fun onClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.footer_item, parent, false)
                .also {
                    val holder = MyHolder(it)
                    holder.itemView.setOnClickListener {
                        listener.onClick(holder.absoluteAdapterPosition)
                    }
                    return holder
                }
    }

    override fun getItemCount() = currentList.size + 2

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder.itemView){
            val item = when(position){
                itemCount - 1 -> currentList.first()
                0 -> currentList.last()
                else -> getItem(position-1)
            }
            tv_music_name.text = item.name
            tv_singer.text = item.singer
        }
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}