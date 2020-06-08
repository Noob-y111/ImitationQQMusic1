package com.example.imitationqqmusic.view.detail.play

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.databinding.PlayFragmentBinding
import com.example.imitationqqmusic.model.bean.SongItem
import com.example.imitationqqmusic.model.tools.DpPxUtils
import com.example.imitationqqmusic.model.tools.ScreenUtils
import com.example.imitationqqmusic.model.tools.TimeFormat
import com.example.imitationqqmusic.service.Connection.Companion.player
import com.example.imitationqqmusic.view.detail.DetailModel
import com.example.imitationqqmusic.view.main.MainViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class PlayFragment() : Fragment() {

    companion object {
        fun newInstance() = PlayFragment()
    }

    private lateinit var viewModel: DetailModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: PlayFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = PlayFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = MainViewModel.getInstance(requireActivity(), requireActivity().application)
        binding.ivDetailMusicImage.layoutParams.height =
                (ScreenUtils.getWidth(requireActivity()) - DpPxUtils.dp2Px(requireContext(), 50.0f)) - 1

//        binding.mtvDetailName.isSelected = true

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
                        .listener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                MainScope().launch {
                                    val bitmap = resources.getDrawable(R.drawable.default_image, null).toBitmap()
                                    binding.ivDetailMusicImage.setImageBitmap(bitmap)
                                    viewModel.setBitmap(bitmap)
                                }
                                return false
                            }

                            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                MainScope().launch {
                                    binding.ivDetailMusicImage.setImageBitmap(resource!!)
                                    viewModel.setBitmap(resource)
                                }
                                return false
                            }
                        })
                        .submit()
//                        .get()
//                        .also {
//                            MainScope().launch {
//                                binding.ivDetailMusicImage.setImageBitmap(it)
//                                viewModel.setBitmap(it)
//                            }
//                        }
            }).start()
        })

        mainViewModel.playPosition.observe(viewLifecycleOwner, Observer {
            binding.sbProgress.progress = it
            binding.tvCurrentTime.text = TimeFormat.timeFormat(it)
        })

        mainViewModel.playOrPause.observe(viewLifecycleOwner, Observer {
            if (player == null) return@Observer
            if (it) {
                binding.ivPlayPause.setImageResource(R.drawable.play_selector)
                player!!.pauseMusic()
            } else {
                binding.ivPlayPause.setImageResource(R.drawable.pause_selector)
                player!!.playMusic()
            }
        })

        binding.ivPlayPause.setOnClickListener {
            mainViewModel.changePlayOrPause(player)
        }

        mainViewModel.duration.observe(viewLifecycleOwner, Observer {
            binding.sbProgress.max = it
            binding.tvDurationTime.text = TimeFormat.timeFormat(it)
        })

        binding.ivDetailMusicImage.setOnClickListener {
            println("================binding.ivDetailMusicImage.width: ${binding.ivDetailMusicImage.width}")
            println("================binding.ivDetailMusicImage.height: ${binding.ivDetailMusicImage.height}")
        }

        binding.ivNext.setOnClickListener {
            val position = player?.next()
            if (position == -1) return@setOnClickListener
            val map = mainViewModel.currentSong.value
            val songItem = (map?.get("list") as ArrayList<SongItem>)[position!!]
            viewModel.changeSong(songItem)
            mainViewModel.changeCurrentSongPosition(position+1)
        }


        binding.ivLast.setOnClickListener {
            val position = player?.last()
            if (position == -1) return@setOnClickListener
            val map = mainViewModel.currentSong.value
            val songItem = (map?.get("list") as ArrayList<SongItem>)[position!!]
            viewModel.changeSong(songItem)
            mainViewModel.changeCurrentSongPosition(position+1)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
