package com.example.imitationqqmusic.view.detail

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.databinding.ActivityDetailBinding
import com.example.imitationqqmusic.model.bean.SongItem
import com.example.imitationqqmusic.view.detail.lyric.LyricFragment
import com.example.imitationqqmusic.view.detail.play.PlayFragment
import com.google.android.material.tabs.TabLayoutMediator
import jp.wasabeef.blurry.Blurry

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailModel

    fun setStatusBar(){
        val view = window.decorView
        view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        view.fitsSystemWindows = true
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = DetailModel.newInstances(this)

        binding.vp2Detail.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2

            override fun createFragment(position: Int): Fragment {
                return when(position){
                    0 -> PlayFragment()
                    else -> LyricFragment()
                }
            }
        }

        TabLayoutMediator(
                binding.tabs,
                binding.vp2Detail,
                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    when(position){
                        0 -> tab.setText(R.string.detail_music)
                        1 -> tab.setText(R.string.detail_lyric)
                    }
                }
        ).attach()

        viewModel.albumBitmap.observe(this, Observer {
            Blurry.with(this)
                    .radius(10)
                    .async()
                    .from(it)
                    .into(binding.ivBackground)
        })

        intent?.let {
            it.getParcelableArrayListExtra<SongItem>("song")?.let { list ->
                viewModel.changeSong(list[0])
            }
        }

        binding.detailToolbar.setNavigationIcon(R.drawable.close)
        binding.detailToolbar.setNavigationOnClickListener {
            finish()
        }

        var isChecked = true
        binding.detailToolbar.inflateMenu(R.menu.detail_menu)
        binding.detailToolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.share -> {
                    Toast.makeText(this, "click...", Toast.LENGTH_SHORT).show()
                }

                R.id.love -> {
                    if (isChecked){
                        it.setIcon(R.drawable.like_selected)
                    }else{
                        it.setIcon(R.drawable.like)
                    }
                    isChecked = !isChecked
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.no_animation, R.anim.detail_dialog_exit)
    }
}
