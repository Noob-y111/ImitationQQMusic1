package com.example.imitationqqmusic.adapter

import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.model.VolleyInstance
import com.example.imitationqqmusic.model.bean.SongItem
import com.example.imitationqqmusic.view.detail.DetailActivity
import com.example.imitationqqmusic.view.detail.MusicDetail
import com.example.imitationqqmusic.view.main.MainActivity
import kotlinx.android.synthetic.main.footer_item.view.*
import java.util.ArrayList

class FooterPagerAdapter(private val listener: OnItemClick, private val fragmentManager: FragmentManager): ListAdapter<SongItem, FooterPagerAdapter.MyHolder>(Compare) {

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
                        val songItem = getItem(holder.absoluteAdapterPosition - 1)
                        val musicDetail = MusicDetail(songItem)
//                        musicDetail.show()
//                        println("================songItem: $songItem")
//                        Intent(holder.itemView.context, DetailActivity::class.java).apply {
//                            putParcelableArrayListExtra("song", ArrayList(arrayOf(songItem).toList()))
//                            holder.itemView.context.startActivity(this)
////                            overridePendingTransition()
//
//                        }
                    }
                    return holder
                }
    }

    override fun getItemCount() = if (currentList.size == 0) 0 else currentList.size + 2

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder.itemView){
            val item = when(position){
                itemCount - 1 -> currentList.first()
                0 -> currentList.last()
                else -> getItem(position-1)
            }
            tv_music_name.text = item.name
            tv_singer.text = item.singer
            if (item.isFromInternet){

            }else{
                Glide.with(this)
                        .load(item.albumPath)
                        .placeholder(R.drawable.default_image)
                        .into(music_image)
            }
        }
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}