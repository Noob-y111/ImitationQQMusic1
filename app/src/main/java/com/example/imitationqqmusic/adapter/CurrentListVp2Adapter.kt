package com.example.imitationqqmusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imitationqqmusic.R
import kotlinx.android.synthetic.main.item_of_recycler.view.*

class CurrentListVp2Adapter(private val callback: InitRecyclerView): RecyclerView.Adapter<CurrentListVp2Adapter.MyHolder>() {
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface InitRecyclerView{
        fun init(v: RecyclerView, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.item_of_recycler, parent, false).also {
            return MyHolder(it)
        }
    }

    override fun getItemCount() = 2

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.itemView.recycler_list.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.itemView.recycler_list.addItemDecoration(DividerItemDecoration(holder.itemView.context, DividerItemDecoration.VERTICAL))
        callback.init(holder.itemView.recycler_list, position)
    }
}