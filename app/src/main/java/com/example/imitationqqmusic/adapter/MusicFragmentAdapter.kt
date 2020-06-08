package com.example.imitationqqmusic.adapter

import android.annotation.SuppressLint
import android.content.Context
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
import com.example.imitationqqmusic.model.bean.MusicFragmentData
import com.example.imitationqqmusic.model.tools.DpPxUtils
import com.example.imitationqqmusic.model.tools.ScreenUtils
import kotlinx.android.synthetic.main.list_item.view.*

class MusicFragmentAdapter(private val width: Int, private val context: Context, private val listener: OnItemClick):
        ListAdapter<MusicFragmentData, MusicFragmentAdapter.MyHolder>(Compare) {

    object Compare: DiffUtil.ItemCallback<MusicFragmentData>(){
        override fun areItemsTheSame(oldItem: MusicFragmentData, newItem: MusicFragmentData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: MusicFragmentData, newItem: MusicFragmentData): Boolean {
            return newItem == oldItem
        }

    }

    private var itemWidth: Int = (width - DpPxUtils.dp2Px(context, 40.0f)) / 3

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnItemClick{
        fun onClick(item: MusicFragmentData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false).also {
            val holder = MyHolder(it)
            holder.itemView.setOnClickListener {
                listener.onClick(getItem(holder.absoluteAdapterPosition))
            }
            return holder
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder.itemView){
            val item = getItem(position)
//            val layout = item_card.layoutParams
//            layout.width = itemWidth
//            layout.height = itemWidth
//            item_card.layoutParams = layout

            Glide.with(this)
                    .load(item.smallImageUrl)
                    .placeholder(R.drawable.shimmer_bg)
                    .error(R.drawable.shimmer_bg)
                    .into(iv_list_item)

            when(item.songItem.size){
                1 -> {
                    tv_first.text = "1.${item.songItem[0].name} - ${item.songItem[0].singer}"
                }

                2 -> {
                    tv_first.text = "1.${item.songItem[0].name} - ${item.songItem[0].singer}"
                    tv_second.text = "2.${item.songItem[1].name} - ${item.songItem[1].singer}"
                }

                3 -> {
                    tv_first.text = "1.${item.songItem[0].name} - ${item.songItem[0].singer}"
                    tv_second.text = "2.${item.songItem[1].name} - ${item.songItem[1].singer}"
                    tv_third.text = "3.${item.songItem[2].name} - ${item.songItem[2].singer}"
                }
            }

        }
    }
}