package com.example.imitationqqmusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.model.bean.Album
import kotlinx.android.synthetic.main.music_fragment_item.view.*
import kotlinx.android.synthetic.main.onloading_item.view.*

class RecyclerSimpleAdapter(private val width: Int, private val clickListener: OnItemClick): ListAdapter<Album, RecyclerView.ViewHolder>(Compare) {
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class Footer(itemView: View) : RecyclerView.ViewHolder(itemView)
    object Compare: DiffUtil.ItemCallback<Album>(){
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.albumId == newItem.albumId
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }

    }

    companion object{
        const val FOOTER = 1
        const val NORMAL = 0
    }

    var state: State? = State.Gone
    var listener: OnErrorClickCallBack? = null

    enum class State{
        Loading, Complete, Error, Gone
    }

    interface OnItemClick{
        fun onClick(album: Album)
    }

    override fun getItemCount() = if (currentList.size == 0) 0 else currentList.size + 1

    override fun getItemViewType(position: Int): Int {
        return when(position){
            itemCount - 1 -> FOOTER
            else -> NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        when(viewType){
            FOOTER -> {
                LayoutInflater.from(parent.context).inflate(R.layout.onloading_item, parent, false).also {
                    return Footer(it)
                }
            }

            else -> {
                LayoutInflater.from(parent.context).inflate(R.layout.music_fragment_item, parent, false).also {
                    val holder = MyHolder(it)
                    holder.itemView.setOnClickListener {view ->
                        val item = getItem(holder.absoluteAdapterPosition)
                        clickListener.onClick(item)
                    }
                    return holder
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is MyHolder -> {
                with(holder.itemView) {
                    val item = getItem(position)
                    Glide.with(this)
                            .load(item.image)
                            .placeholder(R.drawable.shimmer_bg)
                            .error(R.drawable.shimmer_bg)
                            .into(iv_album)

                    tv_name.text = item.title
                }
            }

            else -> {
                if (state == null){
                    holder.itemView.visibility = View.GONE
                }else{
                    (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                    holder.itemView.visibility = View.VISIBLE
                    when(state){
                        State.Loading -> {
                            with(holder.itemView){
                                on_loading_pb.visibility = View.VISIBLE
                                on_loading_tv.text = resources.getText(R.string.on_loading)
                            }
                        }

                        State.Error -> {
                            with(holder.itemView){
                                on_loading_pb.visibility = View.GONE
                                on_loading_tv.text = resources.getText(R.string.loading_error)
                                this.setOnClickListener {
                                    listener?.onErrorClick()
                                }
                            }
                        }

                        State.Complete -> {
                            with(holder.itemView){
                                on_loading_pb.visibility = View.GONE
                                on_loading_tv.text = resources.getText(R.string.loading_complete)
                            }
                        }
                        else -> {
                            holder.itemView.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    interface OnErrorClickCallBack{
        fun onErrorClick()
    }
}