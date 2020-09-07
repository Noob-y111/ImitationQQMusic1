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
import com.example.imitationqqmusic.model.bean.Album
import kotlinx.android.synthetic.main.onloading_item.view.*
import kotlinx.android.synthetic.main.singer_item.view.*

class AlbumAdapter: ListAdapter<Album, RecyclerView.ViewHolder>(Diff) {

    object Diff: DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.albumId == newItem.albumId
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return newItem == oldItem
        }

    }

    enum class State {
        Loading, Complete, Error, Gone
    }

    var state: State? = State.Gone
    var listener: OnErrorClickCallBack? = null

    interface OnErrorClickCallBack {
        fun onErrorClick()
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class Footer(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object{
        private const val FOOTER = 0
        private const val NORMAL = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size){
            FOOTER
        }else{
            NORMAL
        }
    }


    override fun getItemCount(): Int {
        return if (currentList.size == 0) 0 else currentList.size + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == NORMAL){
            LayoutInflater.from(parent.context).inflate(R.layout.singer_item, parent, false).also {
                val holder = Holder(it)
                holder.itemView.setOnClickListener {
                    val album = getItem(holder.absoluteAdapterPosition) as Album
                    val bundle = Bundle()
                    bundle.putString("title", album.title)
                    bundle.putString("imagePath", album.image)
                    bundle.putInt("apiKind", 4)
                    bundle.putInt("type", album.albumId.toInt())
                    Navigation.findNavController(holder.itemView)
                            .navigate(R.id.action_newOfDayFragment_to_musicListFragment, bundle)
                }
                return holder
            }
        }else{
            LayoutInflater.from(parent.context).inflate(R.layout.onloading_item, parent, false).also {
                return Footer(it)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is Holder -> {
                with(holder.itemView){
                    val item = getItem(position)
                    Glide.with(this)
                            .load(item.image)
                            .placeholder(R.drawable.shimmer_bg)
                            .error(R.drawable.shimmer_bg)
                            .into(iv_singer_head)
                    tv_singer_name.text = item.title
                }
            }

            else -> {
                if (state == null){
                    holder.itemView.visibility = View.GONE
                }else{
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
}