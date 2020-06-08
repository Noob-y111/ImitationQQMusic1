package com.example.imitationqqmusic.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.model.bean.Singer
import kotlinx.android.synthetic.main.singer_item.view.*

class SingerAdapter: ListAdapter<Singer, SingerAdapter.MyHolder>(Compare) {

    object Compare: DiffUtil.ItemCallback<Singer>(){
        override fun areItemsTheSame(oldItem: Singer, newItem: Singer): Boolean {
            return oldItem.tingId == newItem.tingId
        }

        override fun areContentsTheSame(oldItem: Singer, newItem: Singer): Boolean {
            return newItem == oldItem
        }

    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.singer_item, parent, false).also {
            val holder = MyHolder(it)
            holder.itemView.setOnClickListener {view ->
                val singer = getItem(holder.absoluteAdapterPosition)
                val bundle = Bundle()
                bundle.putString("title", singer.name)
                bundle.putString("imagePath", singer.image)
                bundle.putInt("type", singer.tingId.toInt())
                bundle.putInt("apiKind", 3)
                Navigation.findNavController(view).navigate(R.id.action_singerFragment_to_musicListFragment, bundle)
            }
            return holder
        }
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder.itemView){
            val singer = getItem(position)
            Glide.with(this)
                    .load(singer.image)
                    .placeholder(R.drawable.shimmer_bg)
                    .error(R.drawable.shimmer_bg)
                    .into(iv_singer_head)
            tv_singer_name.text = singer.name
        }
    }
}