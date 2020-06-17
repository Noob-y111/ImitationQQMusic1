package com.example.imitationqqmusic.view.rankfragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.adapter.MusicFragmentAdapter
import com.example.imitationqqmusic.base.BaseFragment
import com.example.imitationqqmusic.databinding.RankFragmentBinding
import com.example.imitationqqmusic.model.bean.MusicFragmentData
import com.example.imitationqqmusic.model.tools.ScreenUtils

class RankFragment : BaseFragment() {

    private lateinit var binding: RankFragmentBinding

    companion object {
        fun newInstance() = RankFragment()
    }

    private lateinit var viewModel: RankViewModel

    override fun getToolBarId() = R.id.toolbar_rank

    override fun setRootView(): View {
        binding = RankFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getTitle() = "详情"

    override fun initView() {
        binding.toolbarRank.setNavigationIcon(R.drawable.black_back)
        binding.toolbarRank.setNavigationOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment).navigateUp()
        }
        viewModel = ViewModelProvider(this).get(RankViewModel::class.java)

        val adapter = MusicFragmentAdapter(
                ScreenUtils.getWidth(requireActivity()),
                requireContext(),
                object : MusicFragmentAdapter.OnItemClick{
                    override fun onClick(item: MusicFragmentData) {
                        val bundle = Bundle()
                        bundle.putString("title", item.name)
                        bundle.putInt("type", item.type)
                        bundle.putString("imagePath", item.bigImageUrl)
                        viewModel.type.value?.let { bundle.putInt("apiKind", it) }
                        toMusicList(bundle)
                    }
                }
        )

        with(binding.recyclerRank){
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        if (Build.VERSION.SDK_INT >= 23){
            binding.refresh.setColorSchemeColors(resources.getColor(R.color.colorPrimary, null))
        }

        binding.refresh.setProgressViewOffset(true, 0, (ScreenUtils.getHeight(requireActivity()) * 0.09).toInt())
        binding.refresh.setOnRefreshListener {
            viewModel.isFromUser = true
            viewModel.getData(requireContext())
        }

        viewModel.list.observe(viewLifecycleOwner, Observer {
            println("=========================Observer")
            adapter.submitList(it){
                binding.refresh.isRefreshing = false
                if (viewModel.isFromUser){
                    Toast.makeText(requireContext(), "刷新成功~", Toast.LENGTH_SHORT).show()
                    viewModel.isFromUser = false
                }
            }
        })

        viewModel.type.observe(viewLifecycleOwner, Observer {
            if (viewModel.list.value == null)
                viewModel.getData(requireContext())
            else{
                if (viewModel.lastType == it){
                    return@Observer
                }else{
                    viewModel.getData(requireContext())
                }
            }
            viewModel.lastType = it
        })

        arguments?.let {
            viewModel.changeType(it.getInt("type"))
            binding.toolbarRank.title = it.getString("title", "详情")
        }
        binding.refresh.isRefreshing = true
    }

    override fun toMusicList(bundle: Bundle) {
        Navigation.findNavController(requireActivity(), R.id.fragment)
                .navigate(R.id.action_rankFragment_to_musicListFragment, bundle)
    }

}
