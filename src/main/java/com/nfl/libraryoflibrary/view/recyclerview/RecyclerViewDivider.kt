package com.nfl.libraryoflibrary.view.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.nfl.libraryoflibrary.R

class RecyclerViewDivider(context: Context, orientation: Int) : ItemDecoration() {
    private var bgTransparent = false
    private var transparentDrawable: Drawable? = null
    private var mPaint: Paint? = null
    private var mDivider: Drawable?
    private var mDividerHeight = 2 //分割线高度，默认为1px

    //列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    private val mOrientation: Int

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    constructor(context: Context, orientation: Int, @DrawableRes drawableId: Int) : this(
        context,
        orientation
    ) {
        mDivider = ContextCompat.getDrawable(context, drawableId)
        mDividerHeight = mDivider!!.intrinsicHeight
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    constructor(context: Context, orientation: Int, dividerHeight: Int, dividerColor: Int) : this(
        context,
        orientation
    ) {
        mDividerHeight = dividerHeight
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = dividerColor
        mPaint!!.style = Paint.Style.FILL
    }

    constructor(
        context: Context,
        orientation: Int,
        dividerHeight: Int,
        dividerColor: Int,
        alpha: Int
    ) : this(context, orientation, dividerHeight, dividerColor) {
        mPaint!!.alpha = alpha
    }

    constructor(
        context: Context,
        orientation: Int,
        dividerHeight: Int,
        dividerColor: Int,
        bgTransparent: Boolean
    ) : this(context, orientation, dividerHeight, dividerColor) {
        this.bgTransparent = bgTransparent
    }

    //获取分割线尺寸
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val curPosition = parent.getChildLayoutPosition(view)
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            // 最后一项不应该设置
            if (parent.layoutManager is GridLayoutManager) {
                val spanCount = (parent.layoutManager as GridLayoutManager?)!!.spanCount
                if (curPosition / spanCount * spanCount + spanCount >= state.itemCount) {
                    // 说明是最后一行,不应该设置
                    outRect[0, 0, 0] = 0
                } else {
                    outRect[0, 0, 0] = mDividerHeight
                }
            } else {
                if (curPosition == state.itemCount - 1) {
                    outRect[0, 0, 0] = 0
                } else {
                    outRect[0, 0, 0] = mDividerHeight
                }
            }
        } else {
            if (parent.layoutManager is GridLayoutManager) {
                val spanCount = (parent.layoutManager as GridLayoutManager?)!!.spanCount
                if ((curPosition + 1) % spanCount == 0) {
                    // 说明在这里换行，不要绘制了
                    outRect[0, 0, 0] = 0
                } else {
                    outRect[0, 0, mDividerHeight] = 0
                }
            } else {
                outRect[0, 0, mDividerHeight] = 0
            }
        }
    }

    //绘制分割线
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent, state)
        } else {
            drawHorizontal(c, parent)
        }
    }

    /**
     * 绘制纵向列表时的分隔线  这时分隔线是横着的
     * 每次 left相同，top根据child变化，right相同，bottom也变化
     *
     * @param canvas
     * @param parent
     */
    private fun drawVertical(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.measuredWidth - parent.paddingRight
        val childSize = parent.childCount
        // parent.getLayoutManager().isViewPartiallyVisible(parent.)
        for (i in 0 until childSize - 1) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + mDividerHeight
            if (bgTransparent) {
                if (null == transparentDrawable) {
                    transparentDrawable =
                        parent.context.resources.getDrawable(R.drawable.transparent)
                    transparentDrawable?.setBounds(left, top, right, bottom)
                }
                transparentDrawable!!.draw(canvas)
            } else {
                if (mDivider != null) {
                    mDivider!!.setBounds(left, top, right, bottom)
                    mDivider!!.draw(canvas)
                }
                if (mPaint != null) {
                    canvas.drawRect(
                        left.toFloat(),
                        top.toFloat(),
                        right.toFloat(),
                        bottom.toFloat(),
                        mPaint!!
                    )
                }
            }
        }
    }

    /**
     * 绘制横向列表时的分隔线  这时分隔线是竖着的
     * l、r 变化； t、b 不变
     *
     * @param canvas
     * @param parent
     */
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + layoutParams.rightMargin
            val right = left + mDividerHeight
            if (mDivider != null) {
                mDivider!!.setBounds(left, top, right, bottom)
                mDivider!!.draw(canvas)
            }
            if (mPaint != null) {
                canvas.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    mPaint!!
                )
            }
            if (bgTransparent) {
                if (null == transparentDrawable) {
                    transparentDrawable =
                        parent.context.resources.getDrawable(R.drawable.transparent)
                    transparentDrawable?.setBounds(left, top, right, bottom)
                }
                transparentDrawable!!.draw(canvas)
            }
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }

    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context
     * @param orientation 列表方向
     */
    init {
        require(!(orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL)) { "请输入正确的参数！" }
        mOrientation = orientation
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }
}