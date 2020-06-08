package com.example.imitationqqmusic.view.singer

import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.adapter.SingerAdapter
import com.example.imitationqqmusic.base.BaseFragment
import com.example.imitationqqmusic.databinding.SingerFragmentBinding

class SingerFragment : BaseFragment() {

    companion object {
        fun newInstance() = SingerFragment()
    }

    private lateinit var binding: SingerFragmentBinding
    private lateinit var viewModel: SingerViewModel

    override fun getToolBarId() = R.id.toolbar_singer

    override fun getTitle() = "歌手"

    override fun setRootView(): View {
        binding = SingerFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
        binding.toolbarSinger.setNavigationIcon(R.drawable.black_back)
        binding.toolbarSinger.setNavigationOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment).navigateUp()
        }

        viewModel = ViewModelProvider(this).get(SingerViewModel::class.java)

        val adapter = SingerAdapter()
        binding.recyclerSinger.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.singerList.observe(this, Observer {
            adapter.submitList(it){
                binding.refresh.isRefreshing = false
                if (viewModel.isFromUser){
                    Toast.makeText(requireContext(), "刷新成功~", Toast.LENGTH_SHORT).show()
                    viewModel.isFromUser = false
                }
            }
        })

        if (viewModel.singerList.value == null){
            binding.refresh.isRefreshing = true
            viewModel.updateSingerList(requireContext())
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
            binding.refresh.setColorSchemeColors(resources.getColor(R.color.colorPrimary, null))

        binding.refresh.setOnRefreshListener {
            viewModel.isFromUser = true
            viewModel.updateSingerList(requireContext())
        }
    }
}