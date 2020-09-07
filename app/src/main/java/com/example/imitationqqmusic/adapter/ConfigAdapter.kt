package com.example.imitationqqmusic.adapter

import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.model.room_bean.User
import com.example.imitationqqmusic.view.login.LoginActivity
import kotlinx.android.synthetic.main.config_user.view.*

class ConfigAdapter(private val user: User): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class NormalHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    companion object{
        private const val USER = 0
        private const val NORMAL = 1
    }

    private val configs: Array<String> = arrayOf("头像", "昵称", "账号", "修改密码", "切换账号", "退出")
    private val userInfo = user.toStringArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.config_user, parent, false).also {
            val holder = NormalHolder(it)
            if (user.userId == "-1") holder.itemView.isClickable = false
            else {
                holder.itemView.setOnClickListener {
                    when(holder.absoluteAdapterPosition){
                        0 -> {

                        }

                        1 -> {

                        }

                        2 -> {

                        }

                        3 -> {

                        }

                        4 -> {
//                            val intent = Intent(parent.context, LoginActivity::class.java)
//                            parent.context.startActivity(intent)
                        }

                        5 -> {

                        }
                    }
                }
            }
            return holder
        }
    }

    override fun getItemCount() = configs.size

    override fun getItemViewType(position: Int): Int {
        return if (position != 0) NORMAL else USER
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder.itemView) {
            if (position != 0) {
                iv_header.visibility = View.GONE
                if (position < userInfo.size) {
                    val span = SpannableString(configs[position] + ": " + userInfo[position])
                    span.setSpan(ForegroundColorSpan(Color.BLACK), 0, configs[position].length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    tv_config_name.text = span
                }else{
                    val span = SpannableString(configs[position])
                    span.setSpan(ForegroundColorSpan(Color.BLACK), 0, configs[position].length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    tv_config_name.text = span
                }
            } else {
                iv_header.visibility = View.VISIBLE
                val path = if (user.userHead == null || user.userHead == "") R.drawable.default_image else user.userHead
                Glide.with(this)
                        .load(path)
                        .placeholder(R.drawable.shimmer_bg)
                        .error(R.drawable.shimmer_bg)
                        .into(iv_header)
                val span = SpannableString(configs[position])
                span.setSpan(ForegroundColorSpan(Color.BLACK), 0, configs[position].length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_config_name.text = span
            }
        }
    }
}