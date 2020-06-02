package com.example.imitationqqmusic.view.detail

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.databinding.LayoutDetailBinding
import com.example.imitationqqmusic.model.bean.SongItem
import com.example.imitationqqmusic.view.detail.lyric.LyricFragment
import com.example.imitationqqmusic.view.detail.play.PlayFragment
import com.google.android.material.tabs.TabLayoutMediator
import jp.wasabeef.blurry.Blurry

class MusicDetail(private val songItem: SongItem): DialogFragment() {

    private lateinit var binding: LayoutDetailBinding
    private lateinit var viewModel: DetailModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LayoutDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        viewModel.albumBitmap.observe(viewLifecycleOwner, Observer {
            Blurry.with(requireContext())
                    .radius(10)
                    .async()
                    .from(it)
                    .into(binding.ivBackground)
        })
        viewModel.changeSong(songItem)

        binding.detailToolbar.setNavigationIcon(R.drawable.close)
        binding.detailToolbar.setNavigationOnClickListener {
            dismiss()
        }

        var isChecked = true
        binding.detailToolbar.inflateMenu(R.menu.detail_menu)
        binding.detailToolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.share -> {
                    Toast.makeText(requireContext(), "click...", Toast.LENGTH_SHORT).show()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val window = dialog?.window
        window?.let {
            val point = Point()
            it.windowManager?.defaultDisplay?.getSize(point)
            it.attributes.height = point.y
            it.attributes.width = point.x
            it.setGravity(Gravity.BOTTOM)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setWindowAnimations(R.style.detail_dialog_animation)
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            it.statusBarColor = Color.TRANSPARENT
        }
    }
}