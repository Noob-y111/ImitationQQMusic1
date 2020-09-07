package com.example.imitationqqmusic.view.listfragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.iterator
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.adapter.MusicListAdapter
import com.example.imitationqqmusic.adapter.RecyclerSimpleAdapter
import com.example.imitationqqmusic.base.BaseFragment
import com.example.imitationqqmusic.custom.AppbarStateChangedListener
import com.example.imitationqqmusic.custom.Decoration
import com.example.imitationqqmusic.databinding.MusicListFragmentBinding
import com.example.imitationqqmusic.model.GetDataModel
import com.example.imitationqqmusic.model.bean.SongItem
import com.example.imitationqqmusic.model.tools.ScreenUtils
import com.example.imitationqqmusic.service.Connection
import com.example.imitationqqmusic.view.main.MainViewModel
import com.google.android.material.appbar.AppBarLayout

class MusicListFragment : BaseFragment() {

    private lateinit var binding: MusicListFragmentBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModel: MusicListViewModel
    private val listAdapter = MusicListAdapter(object : MusicListAdapter.OnListItemClickListener {
        override fun onListItemClick(position: Int) {
            var list: ArrayList<SongItem> = ArrayList()
            viewModel.list.value?.let {
                list = it
            }
            if (list.size == 0) return
            val map = HashMap<String, Any>()
            map["list"] = list
            map["position"] = position
            mainViewModel.changeCurrentSong(map)
        }
    })
    private val receiver = Receiver()

    companion object {
        fun newInstance() = MusicListFragment()
    }

    override fun getToolBarId() = R.id.test_toolbar

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        setStatusWithConfig(0)
        super.setTransparentStatusBar(0, Color.TRANSPARENT)
        binding.ivHeader.layoutParams.height = (ScreenUtils.getHeight(requireActivity()) * 0.4f).toInt()

        mainViewModel = MainViewModel.getInstance(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory(requireActivity().application))
                .get(MusicListViewModel::class.java)

        viewModel.kind.observe(viewLifecycleOwner, Observer {
//            when (it) {
//                "经典老歌榜" -> {
//
//                }
//                "热歌榜" -> {
//
//                }
//                "新歌榜" -> {
//
//                }
//                "情歌对唱榜" -> {
//
//                }
//                "欧美金曲榜" -> {
//
//                }
//                "网络歌曲榜" -> {
//
//                }
//                "影视金曲榜" -> {
//
//                }
//            }
        })

        var title: String = "音乐列表"
        var type = -1
        var path: Any? = R.drawable.default_image
        var apiKind: Int = -1
        arguments?.let {
            title = it.getString("title")!!
            type = it.getInt("type")
            path = it.getString("imagePath")
            apiKind = it.getInt("apiKind")
        }

        binding.testToolbar.title = title
        binding.testToolbar.setNavigationIcon(R.drawable.back)
        binding.testToolbar.setNavigationOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment).navigateUp()
        }

        val layout = binding.appbar.getChildAt(0).layoutParams as AppBarLayout.LayoutParams
        layout.scrollFlags = 0
        viewModel.list.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it) {
                val flag = AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED or AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                layout.scrollFlags = flag
                binding.appbar.getChildAt(0).layoutParams = layout
                binding.pbListOnLoad.visibility = View.GONE

                Connection.player?.getCurrentSong()?.let {item ->
                    val index = it.indexOf(item)
                    if (index != -1){
                        listAdapter.setCheckedIndex(index + 1)
                    }
                }
            }
        })

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
//            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            addItemDecoration(Decoration())
            this.adapter = listAdapter
        }

        binding.pbListOnLoad.visibility = View.VISIBLE

        //todo
        viewModel.getData(type, path, apiKind)

        viewModel.state.observe(viewLifecycleOwner, Observer {
            if (it == AppbarStateChangedListener.State.COLLAPSED) {
                binding.testToolbar.navigationIcon?.alpha = 255
                binding.testToolbar.setTitleTextColor(Color.WHITE)
                for (i in binding.testToolbar.menu) {
                    i.icon?.alpha = 255
                }
            } else {
                binding.testToolbar.navigationIcon?.alpha = 100
                for (i in binding.testToolbar.menu) {
                    i.icon?.alpha = 100
                }
                val color = Color.WHITE
                binding.testToolbar.setTitleTextColor(Color.argb(100, Color.red(color), Color.green(color), Color.blue(color)))
            }
        })

        binding.appbar.addOnOffsetChangedListener(object : AppbarStateChangedListener() {
            override fun onStateChanged(appbar: AppBarLayout?, state: State) {
                viewModel.setState(state)
            }
        })

        viewModel.firstAlbumPath.observe(viewLifecycleOwner, Observer {
            Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            viewModel.setState(AppbarStateChangedListener.State.EXPANDED)
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            viewModel.setState(AppbarStateChangedListener.State.EXPANDED)
                            return false
                        }
                    })
                    .into(binding.ivHeader)
//            Thread(Runnable {
//                Glide.with(this)
//                        .asBitmap()
//                        .load(it)
//                        .placeholder(R.drawable.shimmer_bg)
//                        .submit()
//                        .get().also {
//                            MainScope().launch {
//                                binding.ivHeader.setImageBitmap(it)
////                                Palette.from(it).generate{ palette ->
////                                    palette ?: run {
////                                        println("=========================null")
////                                    }
////                                    val color = palette?.dominantSwatch
////                                    val rgb = color?.titleTextColor
////                                    viewModel.titleColor = rgb
////                                    viewModel.alpha = Color.alpha(rgb!!)
//                                viewModel.setState(AppbarStateChangedListener.State.EXPANDED)
////                                }
//                            }
//                        }
//            }).start()
        })

        binding.fabJump.setOnClickListener {
            binding.nvContainer.scrollTo(0, 0)
        }

        viewModel.floatingDisplay.observe(viewLifecycleOwner, Observer {
            if (it){
                binding.fabJump.visibility = View.VISIBLE
            }else{
                binding.fabJump.visibility = View.GONE
            }
        })

        binding.nvContainer.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            viewModel.changeDisplayEnable()
        })

        val intentFilter = IntentFilter()
        intentFilter.addAction("change_checked_position")
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, intentFilter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun setRootView(): View {
        binding = MusicListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreateOptionMenuOnToolBar(toolbar: Toolbar?) {
        super.onCreateOptionMenuOnToolBar(toolbar)
        toolbar?.inflateMenu(R.menu.default_white_more)
    }

    override fun onToolBarOptionMenuSelected(menuItem: MenuItem?) {
        super.onToolBarOptionMenuSelected(menuItem)
        Toast.makeText(requireContext(), "click...", Toast.LENGTH_SHORT).show()
    }

    override fun getTitle() = "音乐列表"

//    override fun onPause() {
//        super.onPause()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        setStatusBar()
    }

    inner class Receiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                "change_checked_position" -> {
                    println("=========================change_checked_position")
                    Connection.player?.getCurrentSong()?.let {
                        if (listAdapter.currentList.indexOf(it) != -1){
                            listAdapter.setCheckedIndex(listAdapter.currentList.indexOf(it) + 1)
                        }
                    }
                }
            }
        }
    }

}
