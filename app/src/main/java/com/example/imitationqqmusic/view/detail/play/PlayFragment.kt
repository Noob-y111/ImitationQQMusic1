package com.example.imitationqqmusic.view.detail.play

import android.graphics.Point
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide

import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.databinding.LayoutDetailBinding
import com.example.imitationqqmusic.databinding.PlayFragmentBinding
import com.example.imitationqqmusic.model.tools.DpPxUtils
import com.example.imitationqqmusic.model.tools.ScreenUtils
import com.example.imitationqqmusic.view.detail.DetailModel
import jp.wasabeef.blurry.Blurry
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class PlayFragment : Fragment() {

    companion object {
        fun newInstance() = PlayFragment()
    }

    private lateinit var viewModel: DetailModel
    private lateinit var binding: PlayFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = PlayFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivDetailMusicImage.layoutParams.height =
                (ScreenUtils.getWidth(requireActivity()) - DpPxUtils.dp2Px(requireContext(), 50.0f)) - 1

        viewModel = DetailModel.newInstances(this)
        viewModel.currentSong.observe(viewLifecycleOwner, Observer {
            println("================it: $it")
            binding.mtvDetailName.text = it.name
            binding.tvDetailSinger.text = it.singer
            Thread(Runnable {
                Glide.with(this)
                        .asBitmap()
                        .load(it.albumPath)
                        .placeholder(R.drawable.default_image)
                        .error(R.drawable.default_image)
                        .submit()
                        .get().also {
                            MainScope().launch {
                                binding.ivDetailMusicImage.setImageBitmap(it)
                                viewModel.setBitmap(it)
                            }
                        }
            }).start()
        })

        binding.ivDetailMusicImage.setOnClickListener {
            println("================binding.ivDetailMusicImage.width: ${binding.ivDetailMusicImage.width}")
            println("================binding.ivDetailMusicImage.height: ${binding.ivDetailMusicImage.height}")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
