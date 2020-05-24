package com.example.imitationqqmusic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.model.bean.SongItem
import kotlinx.android.synthetic.main.music_list_item.view.*

class MusicListAdapter : ListAdapter<SongItem, RecyclerView.ViewHolder>(Compare) {

    class NormalHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class FooterHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private var checkedIndex = -1

    object Compare : DiffUtil.ItemCallback<SongItem>() {
        override fun areItemsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return newItem.songMid == oldItem.songMid
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: SongItem, newItem: SongItem): Boolean {
            return newItem === oldItem
        }

    }

    companion object {
        const val NORMAL = 0
        const val HEADER = 1
        const val FOOTER = 2
    }

    override fun getItemCount() = currentList.size + 2
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEADER
            itemCount - 1 -> FOOTER
            else -> NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder: RecyclerView.ViewHolder
        val view: View
        when (viewType) {
            HEADER -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.list_header_item, parent, false)
                holder = HeaderHolder(view)
            }
            FOOTER -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.list_footer_item, parent, false)
                holder = FooterHolder(view)
            }

            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.music_list_item, parent, false)
                holder = NormalHolder(view)
                holder.itemView.setOnClickListener {
                    val holderIndex = holder.absoluteAdapterPosition

                    if (checkedIndex == holderIndex)
                        return@setOnClickListener

                    if (checkedIndex != -1)
                        notifyItemChanged(checkedIndex)

                    this.checkedIndex = holderIndex
                    notifyItemChanged(holderIndex)
                }
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HeaderHolder -> {
                holder.itemView.setOnClickListener {
                    Toast.makeText(holder.itemView.context, "点击了一下", Toast.LENGTH_SHORT).show()
                }
            }

            is FooterHolder -> {

            }

            else -> {
                with(holder.itemView){
                    val item = getItem(position - 1 )
                    tv_item_name.text = item.name
                    tv_item_singer.text = item.singer
                    list_checkbox.isChecked = (position == checkedIndex)
                }
            }
        }
    }
}