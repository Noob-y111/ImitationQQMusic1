package com.example.imitationqqmusic.view.config

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.adapter.ConfigAdapter
import com.example.imitationqqmusic.adapter.CurrentListAdapter
import com.example.imitationqqmusic.base.BaseFragment
import com.example.imitationqqmusic.custom.ConfigDecoration
import com.example.imitationqqmusic.custom.Decoration
import com.example.imitationqqmusic.databinding.ConfigFragmentBinding
import com.example.imitationqqmusic.model.bean.SongItem
import com.example.imitationqqmusic.view.main.MainViewModel

class ConfigFragment : BaseFragment() {

    companion object {
        fun newInstance() = ConfigFragment()
    }

    private lateinit var viewModel: ConfigViewModel
    private lateinit var binding: ConfigFragmentBinding

    override fun getToolBarId() = R.id.config_toolbar

    override fun setRootView(): View {
        binding = ConfigFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getTitle() = "个人中心"

    override fun initView() {
        viewModel = ViewModelProvider(this).get(ConfigViewModel::class.java)

        binding.configToolbar.setNavigationIcon(R.drawable.black_back)
        binding.configToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        with(binding.recyclerConfig){
            layoutManager = LinearLayoutManager(requireContext())
            val adapter = ConfigAdapter(
                    MainViewModel.getInstance(requireActivity(), requireActivity().application).loginUser.value!!
            )
            this.adapter = adapter
            addItemDecoration(ConfigDecoration(intArrayOf(0, 4), arrayOf("个人信息", "其他"), 20))
        }

    }

    override fun onCreateOptionMenuOnToolBar(toolbar: Toolbar?) {
        super.onCreateOptionMenuOnToolBar(toolbar)
        toolbar?.inflateMenu(R.menu.default_black_more)
    }

    override fun onToolBarOptionMenuSelected(menuItem: MenuItem?) {
        super.onToolBarOptionMenuSelected(menuItem)
        when(menuItem?.itemId){
            R.id.white_more -> {

            }
        }
    }

}