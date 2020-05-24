package com.example.imitationqqmusic.view.listfragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.adapter.MusicListAdapter
import com.example.imitationqqmusic.base.BaseFragment
import com.example.imitationqqmusic.custom.AppbarStateChangedListener
import com.example.imitationqqmusic.databinding.MusicListFragmentBinding
import com.example.imitationqqmusic.view.main.MainViewModel
import com.google.android.material.appbar.AppBarLayout
import jp.wasabeef.blurry.Blurry
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MusicListFragment : BaseFragment() {

    private lateinit var binding: MusicListFragmentBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModel: MusicListViewModel

    companion object {
        fun newInstance() = MusicListFragment()
    }

    override fun getToolBarId() = R.id.test_toolbar

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        super.setTransparentStatusBar(0, Color.TRANSPARENT)

        mainViewModel = MainViewModel.getInstance(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory(requireActivity().application))
                .get(MusicListViewModel::class.java)

        binding.testToolbar.title = arguments?.getString("kind", "音乐列表")
        binding.testToolbar.setNavigationIcon(R.drawable.back)
        binding.testToolbar.setNavigationOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment).navigateUp()
        }

        val listAdapter: MusicListAdapter
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            listAdapter = MusicListAdapter()
            this.adapter = listAdapter
        }

        viewModel.list.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it)
            binding.pbListOnLoad.visibility = View.GONE
        })

        binding.pbListOnLoad.visibility = View.VISIBLE
        binding.coordinatorRoot.visibility = View.INVISIBLE
        viewModel.getSongList()

        viewModel.state.observe(viewLifecycleOwner, Observer {
            if (it == AppbarStateChangedListener.State.COLLAPSED){
                binding.testToolbar.navigationIcon?.alpha = 255
                binding.testToolbar.setTitleTextColor(Color.WHITE)
            }else{
                binding.testToolbar.navigationIcon?.alpha = viewModel.alpha
                binding.testToolbar.setTitleTextColor(viewModel.titleColor!!)
            }
        })

        binding.appbar.addOnOffsetChangedListener(object : AppbarStateChangedListener(){
            override fun onStateChanged(appbar: AppBarLayout?, state: State) {
                viewModel.setState(state)
            }
        })

        viewModel.firstAlbumPath.observe(viewLifecycleOwner, Observer {
            Thread(Runnable {
                Glide.with(this)
                        .asBitmap()
                        .load(it)
                        .placeholder(R.drawable.shimmer_bg)
                        .submit()
                        .get().also {
                            MainScope().launch {
                                Blurry.with(requireContext())
                                        .radius(10)
                                        .async()
                                        .from(it)
                                        .into(binding.ivHeader)

                                Palette.from(it).generate{ palette ->
                                    palette ?: run {
                                        println("=========================null")
                                    }
                                    val color = palette?.dominantSwatch
                                    val rgb = color?.bodyTextColor
                                    binding.coordinatorRoot.visibility = View.VISIBLE
                                    viewModel.titleColor = rgb
                                    viewModel.alpha = Color.alpha(rgb!!)
                                    viewModel.setState(AppbarStateChangedListener.State.EXPANDED)
                                }
                            }
                        }
            }).start()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun setRootView(): View {
        binding = MusicListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun toMusicDetail(bundle: Bundle) {

    }

    override fun getTitle() = "音乐列表"

    override fun onPause() {
        super.onPause()
        mainViewModel.setShouldTranslate(false)
    }

}
