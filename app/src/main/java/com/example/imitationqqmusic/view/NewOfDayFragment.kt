package com.example.imitationqqmusic.view

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.adapter.AlbumAdapter
import com.example.imitationqqmusic.adapter.RecyclerSimpleAdapter
import com.example.imitationqqmusic.base.BaseFragment
import com.example.imitationqqmusic.databinding.NewOfDayFragmentBinding
import com.example.imitationqqmusic.view.main.MainViewModel

class NewOfDayFragment : BaseFragment() {

    companion object {
        fun newInstance() = NewOfDayFragment()
    }

    private lateinit var binding: NewOfDayFragmentBinding
    private lateinit var viewModel: NewOfDayViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun getToolBarId() = R.id.toolbar_new

    override fun initView() {

        mainViewModel = MainViewModel.getInstance(requireActivity(), requireActivity().application)
        viewModel = ViewModelProvider(this).get(NewOfDayViewModel::class.java)

        binding.toolbarNew.setNavigationIcon(R.drawable.black_back)
        binding.toolbarNew.setNavigationOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        val adapter = AlbumAdapter()
        with(binding.recyclerNew){
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        viewModel.list.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it){
                if(binding.refresh.isRefreshing){
                    binding.refresh.isRefreshing = false
                }
            }
        })

        viewModel.state.observe(viewLifecycleOwner, Observer{ state ->
            adapter.state = state
            adapter.notifyItemChanged(adapter.itemCount - 1)
            when(state){
                AlbumAdapter.State.Error -> {
                    binding.refresh.isRefreshing = false
                    viewModel.isLoading = false
                }

                AlbumAdapter.State.Complete -> {

                }

                AlbumAdapter.State.Gone -> {

                }

                AlbumAdapter.State.Loading -> {
//                    viewModel.isLoading = true
                }

                else -> { //未知错误

                }
            }
        })

        binding.recyclerNew.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val position = layoutManager.findLastVisibleItemPosition()

                if (dy > 0 && adapter.itemCount-1 == position && !binding.refresh.isRefreshing){
                    viewModel.getList(requireContext())
                }
            }
        })

        binding.refresh.setOnRefreshListener {
            viewModel.resetGetList(requireContext())
        }

        viewModel.list.value ?: run {
            binding.refresh.isRefreshing = true
            viewModel.resetGetList(requireContext())
        }
    }


    override fun setRootView(): View {
        binding = NewOfDayFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getTitle() = "新歌速递"

}