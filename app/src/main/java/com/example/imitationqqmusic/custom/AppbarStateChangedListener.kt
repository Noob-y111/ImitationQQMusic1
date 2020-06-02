package com.example.imitationqqmusic.custom

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

abstract class AppbarStateChangedListener: AppBarLayout.OnOffsetChangedListener {

    enum class State {
        EXPANDED,   //展开
        COLLAPSED,   //折叠
        IDLE   //中间
    }

    private var mCurrentState = State.IDLE

    override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
        if (p1 == 0){
            if (mCurrentState != State.EXPANDED){
                onStateChanged(p0, State.EXPANDED)
            }
            mCurrentState = State.EXPANDED
        }else if (abs(p1) >= p0?.totalScrollRange!!){
            if (mCurrentState != State.COLLAPSED){
                onStateChanged(p0, State.COLLAPSED)
            }
            mCurrentState = State.COLLAPSED
        }else{
            if (mCurrentState != State.IDLE){
                onStateChanged(p0, State.IDLE)
            }
            mCurrentState = State.IDLE
        }
    }

    abstract fun onStateChanged(appbar: AppBarLayout?, state: State)
}