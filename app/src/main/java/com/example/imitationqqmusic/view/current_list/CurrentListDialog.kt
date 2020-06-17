package com.example.imitationqqmusic.view.current_list

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.adapter.CurrentListAdapter
import com.example.imitationqqmusic.adapter.CurrentListVp2Adapter
import com.example.imitationqqmusic.databinding.CurrentListDialogBinding
import com.example.imitationqqmusic.model.bean.SongItem
import com.example.imitationqqmusic.model.tools.DpPxUtils.Companion.dp2Px
import com.example.imitationqqmusic.view.detail.DetailModel
import com.example.imitationqqmusic.view.main.MainViewModel

class CurrentListDialog(
        private val currentList: List<SongItem>,
        private val lastList: List<SongItem>?,
        private val background: Int) : DialogFragment(), CurrentListAdapter.OnItemClickListener {

    private lateinit var binding: CurrentListDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CurrentListDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapterOfCurrent = CurrentListAdapter(this)
        val adapterOfLast = CurrentListAdapter(this)

        binding.vp2.adapter = CurrentListVp2Adapter(object : CurrentListVp2Adapter.InitRecyclerView {
            override fun init(v: RecyclerView, position: Int) {
                when (position) {
                    0 -> {
                        v.adapter = adapterOfCurrent
                        adapterOfCurrent.submitList(currentList)
                    }

                    1 -> {
                        v.adapter = adapterOfLast
                        lastList?.let { adapterOfLast.submitList(it) }
                    }
                }
            }
        })

        binding.vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    setCheckCircle(binding.vp2.currentItem)
                }
            }
        })

        binding.tvPlayStyle.text = "随机播放(${currentList.size}首)"

        for (i in 0..1) {
            val checkBox = CheckBox(requireContext())
            checkBox.isEnabled = false
            checkBox.buttonDrawable = null
            val layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT
            )
            layoutParams.height = dp2Px(requireContext(), 5f)
            layoutParams.weight = dp2Px(requireContext(), 5f).toFloat()
            layoutParams.weight = 1f
            layoutParams.leftMargin = dp2Px(requireContext(), 3f)
            layoutParams.rightMargin = dp2Px(requireContext(), 3f)
            checkBox.background = resources.getDrawable(R.drawable.circle, null)
            binding.circleParent.addView(checkBox, layoutParams)
        }
        (binding.circleParent.getChildAt(0) as CheckBox).isChecked = true

        binding.tvExit.setOnClickListener {
            dismiss()
        }

        binding.dialogRoot.setBackgroundColor(background)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val window = dialog?.window
        window?.let {
            val point = Point()
            it.windowManager?.defaultDisplay?.getSize(point)
            it.attributes.height = (point.y * 0.75).toInt()
            it.attributes.width = point.x
            it.setGravity(Gravity.BOTTOM)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setWindowAnimations(R.style.detail_dialog_animation)
        }
    }

    private fun setCheckCircle(checkIndex: Int) {
        for (i in 0 until binding.circleParent.childCount) {
            (binding.circleParent.getChildAt(i) as CheckBox).isChecked = (i == checkIndex)
        }
    }

    override fun onClick(position: Int, list: List<SongItem>) {
        val map = HashMap<String, Any>()
        map["list"] = list
        map["position"] = position
        MainViewModel.getInstance(requireActivity(), requireActivity().application).changeCurrentSong(map)
        DetailModel.newInstances(this).changeSong(list[position])
        dismiss()
    }

}