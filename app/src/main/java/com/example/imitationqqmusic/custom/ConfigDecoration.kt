package com.example.imitationqqmusic.custom

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.model.tools.DpPxUtils

class ConfigDecoration(private val positions: IntArray, private val text: Array<String>, private val paddingTop: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        (parent.layoutManager as LinearLayoutManager).getPosition(view).let {
            if (it in positions){
                outRect.set(0, DpPxUtils.dp2Px(parent.context, (paddingTop).toFloat()), 0, 0)
            }else{
                outRect.set(0,1,0,0)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val count = parent.childCount
        val textPaint = Paint()
        textPaint.color = parent.context.getColor(R.color.grayTextColor)
        textPaint.textSize = DpPxUtils.dp2Px(parent.context, (paddingTop-5).toFloat()).toFloat()

        val backgroundPaint = Paint()
        backgroundPaint.color = parent.context.getColor(R.color.detail_text_color)

        for (i in 0 until count){
            val view = parent[i]
            (parent.layoutManager as LinearLayoutManager).getPosition(view).let {
                if (it in positions){
                    val top = view.y - DpPxUtils.dp2Px(parent.context, paddingTop.toFloat())
                    val left = view.x + view.paddingLeft
                    val bottom = view.y
                    val right = view.x + view.width - view.paddingRight
                    c.drawText(text[positions.indexOf(it)], left + 10 , bottom - 10, textPaint)
                }else{
                    val top = view.y + 1
                    val bot = view.y
                    val left = view.left
                    val right = view.right
                    c.drawRect(left.toFloat(), top, right.toFloat(), bot, backgroundPaint)
                }
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }

}