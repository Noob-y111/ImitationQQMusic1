package com.example.imitationqqmusic.custom

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.imitationqqmusic.model.tools.DpPxUtils

class Decoration: RecyclerView.ItemDecoration() {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val count = parent.childCount
        val paint = Paint()
        paint.color = Color.GRAY
        for (i in 0 until count){
            val view = parent[i]
            val top = view.y + view.height
            val bottom = view.y + view.height + 1
            val left = view.x + DpPxUtils.dp2Px(parent.context, 10.0f)
            val right = view.x + view.width - DpPxUtils.dp2Px(parent.context, 10.0f)
            c.drawRect(left, top, right, bottom, paint)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(0,0,0,1)
    }
}