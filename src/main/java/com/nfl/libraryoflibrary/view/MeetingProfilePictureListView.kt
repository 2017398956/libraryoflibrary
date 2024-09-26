package com.nfl.libraryoflibrary.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.allViews
import androidx.core.view.contains
import androidx.core.view.get

class MeetingProfilePictureListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val colsCount = 3
    var scaleAnimDuration = 500L
    var onItemScaleChangeListener: OnItemScaleChangeListener? = null
    private val itemViewList = arrayListOf<View>()
    private val viewPositionMap = hashMapOf<View, Int>()
    private val viewScaleMap = hashMapOf<View, Boolean>()
    private val animList = arrayListOf<ObjectAnimator>()
    private val offsetYAnimList = arrayListOf<ObjectAnimator>()
    private lateinit var nullView: View
    var itemSize = 0
        private set
    private var scaledPosition = -1
    private lateinit var scaledView: View
    private val mItemOnClickListener = object : View.OnClickListener {
        override fun onClick(v: View) {
            itemViewList.forEach {
                if (viewScaleMap[it]!!) {
                    scaledPosition = viewPositionMap[it]!!
                    return@forEach
                }
            }
            val position = viewPositionMap[v]!!
            val hasScaled = viewScaleMap[v]!!
            onItemScaleChangeListener?.onItemScaleChanged(position, hasScaled)
            if (!adapter!!.scale) {
                (itemViewList[position] as ViewGroup).let {
                    scaledView = it.get(0)
                }
            }
            animList.forEach {
                it.reverse()
            }
            animList.clear()
            if (scaledPosition == -1) {
                viewScaleMap[v] = true
            } else {
                viewScaleMap[itemViewList[scaledPosition]] = false
                if (scaledPosition == position) {
                    viewScaleMap[v] = false
                } else {
                    viewScaleMap[v] = true
                }
            }
            if (scaledPosition / colsCount != position / colsCount || scaledPosition == position) {
                offsetYAnimList.forEach {
                    it.reverse()
                }
                offsetYAnimList.clear()
            }
            if (scaledPosition != position) {
                if (scaledPosition != -1 && onItemScaleChangeListener != null) {
                    onItemScaleChangeListener!!.onItemScaleChanged(scaledPosition, true)
                    if (!adapter!!.scale) {
                        val itemChildrenView = itemViewList[scaledPosition].allViews.toList().get(0)
                    }
                }
                nullView.visibility = View.VISIBLE
                var anim02: ObjectAnimator? = null
                var anim03: ObjectAnimator? = null
                when (position % colsCount) {
                    0 -> {
                        v.pivotX = 0f
                        v.pivotY = 0f

                        if (position + 1 < itemViewList.size) {
                            anim02 = ObjectAnimator.ofFloat(
                                itemViewList[position + 1],
                                "translationX",
                                0f,
                                itemSize.toFloat()
                            )
                            anim02.setDuration(scaleAnimDuration)
                            anim02.start()
                        }
                        if (position + 2 < itemViewList.size) {
                            anim03 = ObjectAnimator.ofFloat(
                                itemViewList[position + 2],
                                "translationY",
                                0f,
                                itemSize.toFloat()
                            )
                            anim03.setDuration(scaleAnimDuration)
                            anim03.start()
                        }
                    }

                    1 -> {
                        v.pivotX = 0f
                        v.pivotY = 0f

                        if (position + 1 < itemViewList.size) {
                            anim02 = ObjectAnimator.ofFloat(
                                itemViewList[position + 1],
                                "translationX",
                                0f,
                                -itemSize.toFloat() * 2
                            )
                            anim02.setDuration(scaleAnimDuration)
                            anim02.start()

                            anim03 = ObjectAnimator.ofFloat(
                                itemViewList[position + 1],
                                "translationY",
                                0f,
                                itemSize.toFloat()
                            )
                            anim03.setDuration(scaleAnimDuration)
                            anim03.start()
                        }
                    }

                    2 -> {
                        v.pivotX = itemSize.toFloat()
                        v.pivotY = 0f
                        if (position - 1 >= 0) {
                            anim02 = ObjectAnimator.ofFloat(
                                itemViewList[position - 1],
                                "translationX",
                                0f,
                                -itemSize.toFloat()
                            )
                            anim02.setDuration(scaleAnimDuration)
                            anim02.start()

                            anim03 = ObjectAnimator.ofFloat(
                                itemViewList[position - 1],
                                "translationY",
                                0f,
                                itemSize.toFloat()
                            )
                            anim03.setDuration(scaleAnimDuration)
                            anim03.start()
                        }
                    }
                }
                val animVX = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 2.0f)
                val animVY = ObjectAnimator.ofFloat(v, "scaleY", 1.0f, 2.0f)
                animVX.setDuration(scaleAnimDuration)
                animVX.start()
                animVY.setDuration(scaleAnimDuration)
                animVY.start()

                animList.add(animVX)
                animList.add(animVY)
                anim02?.let {
                    animList.add(it)
                }
                anim03?.let {
                    animList.add(it)
                }
                if (offsetYAnimList.isEmpty()) {
                    for (tempIndex in (position / colsCount + 1) * colsCount until itemViewList.size) {
                        val offsetY = ObjectAnimator.ofFloat(
                            itemViewList[tempIndex],
                            "translationY",
                            0f,
                            itemSize.toFloat()
                        )
                        offsetY.setDuration(scaleAnimDuration)
                        offsetY.start()
                        offsetYAnimList.add(offsetY)
                    }
                }
                scaledPosition = position
            } else {
                nullView.visibility = View.GONE
                scaledPosition = -1
            }
        }
    }
    var adapter: BaseAdapter? = null
        set(value) {
            field = value
            post {
                itemViewList.clear()
                viewPositionMap.clear()
                viewScaleMap.clear()
                animList.clear()
                offsetYAnimList.clear()
                removeAllViews()
                drawItems()
            }
        }

    private fun drawItems() {
        if (adapter == null) {
            return
        }
        itemSize = width / colsCount
        val totalNum = adapter!!.getCount()
        for (i in 0 until totalNum) {
            var itemViewWrapView: FrameLayout? = null
            if (!adapter!!.scale) {
                itemViewWrapView = FrameLayout(context)
            }
            var itemView = adapter!!.getView(
                i, null, if (adapter!!.scale) {
                    this
                } else {
                    itemViewWrapView!!
                }
            )
            val layoutParams = LayoutParams(itemSize, itemSize)
            layoutParams.leftMargin = i % colsCount * itemSize
            layoutParams.topMargin = i / colsCount * itemSize
            if (!adapter!!.scale) {
                itemViewWrapView!!.addView(itemView)
                itemView = itemViewWrapView
            }
            viewPositionMap.put(itemView, i)
            viewScaleMap.put(itemView, false)
            itemView.setOnClickListener(mItemOnClickListener)
            itemViewList.add(itemView)
            addView(itemView, layoutParams)
        }
        if (!this::nullView.isInitialized) {
            nullView = View(context)
        }
        nullView.visibility = View.GONE
        val layoutParams = LayoutParams(itemSize, itemSize)
        layoutParams.topMargin = (totalNum + colsCount) / colsCount * itemSize
        nullView.setBackgroundColor(Color.TRANSPARENT)
        if (!contains(nullView)) {
            addView(nullView, layoutParams)
        }
    }

    fun scaleItemView(position: Int) {
        adapter?.let {
            if (position >= 0 && position < it.getCount()) {
                mItemOnClickListener.onClick(itemViewList[position])
            }
        }
    }

    fun getScaleItemViewPosition(): Int {
        return scaledPosition
    }

    abstract class BaseAdapter {

        var scale = true
        private var data: List<Any>

        constructor(data: List<Any>) : this(true, data)

        constructor(scale: Boolean, data: List<Any>) {
            this.scale = scale
            this.data = data
        }

        abstract fun getCount(): Int

        abstract fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    }

    interface OnItemScaleChangeListener {
        fun onItemScaleChanged(position: Int, normal: Boolean)
    }
}
