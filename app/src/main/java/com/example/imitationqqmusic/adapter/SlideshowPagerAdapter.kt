package com.example.imitationqqmusic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imitationqqmusic.R
import kotlinx.android.synthetic.main.slideshow_item.view.*

class SlideshowPagerAdapter: ListAdapter<Any, SlideshowPagerAdapter.MyHolder>(Compare) {

    object Compare: DiffUtil.ItemCallback<Any>(){
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.slideshow_item, parent, false).also {
            val holder = MyHolder(it)
            holder.itemView.setOnClickListener {

            }
            return holder
        }
    }

    override fun getItemCount() = if(currentList.size == 0 ) 0 else currentList.size + 2

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder.itemView){
            val index = when(position){
                0 -> currentList.size - 1

                itemCount - 1 -> 0

                else -> position - 1
            }
            Glide.with(this)
                    .load(getItem(index))
                    .placeholder(R.drawable.shimmer_bg)
                    .into(iv_vp2_image)
        }
    }
}