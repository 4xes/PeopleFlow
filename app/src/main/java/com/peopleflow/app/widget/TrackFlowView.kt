package com.peopleflow.app.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.peopleflow.app.R
import com.peopleflow.app.entities.Bbox
import com.peopleflow.app.entities.Data
import com.peopleflow.app.entities.Point
import mmd.kit.ui.extension.pxFromDp
import mmd.kit.ui.extension.pxFromSp

class TrackFlowView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var textColor: Int = Color.BLACK
    private var rectColor: Int = Color.GREEN
    private lateinit var textPaint: Paint
    private lateinit var rectPaint: Paint
    private var textSize: Float = 0f
    private var lineWidth: Float = 1f
    private var poolRect: List<Rect> = emptyList()
    private lateinit var colors: List<Int>
    private var bound: Rect ?= Rect()

    var data: Data? = null
        set(value) {
            if (value != null) {
                field = value
                if (value.bbox != null && value.bbox.isNotEmpty() && value.bbox.size > poolRect.size) {
                    val dif = value.bbox.size - poolRect.size
                    val difList = mutableListOf<Rect>()
                    for (i in 0 until dif) {
                        difList.add(Rect())
                    }
                    poolRect = poolRect.plus(difList.toList())
                }
                invalidate()
            }
        }

    init {
        initAttributes(context, attrs, defStyleAttr, 0)
        initDimensions(context)
        initPaints()
        initPool()
    }

    private val TAG = this.javaClass.simpleName!!

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data != null && data!!.bbox != null) {
            for (i in 0 until data!!.bbox!!.size) {
                val bbox = data!!.bbox!![i]
                val rect = poolRect[i]

                val _width = width - paddingLeft - paddingRight
                val _height = height - paddingTop - paddingBottom

                rect.left = (paddingLeft + _width * bbox.point1.x).toInt()
                rect.top = (paddingTop + _height * bbox.point1.y).toInt()
                rect.right = (paddingLeft + _width * bbox.point2.x).toInt()
                rect.bottom = (paddingTop + _height * bbox.point2.y).toInt()

                if (rect.isEmpty) {
                    continue
                } else {
                    Log.d(TAG, rect.toString())
                }
                val color = getColor(bbox.id.hashCode())
                rectPaint.color = color
                textPaint.color = color

                canvas.drawRect(rect, rectPaint)
                val id = bbox.id.toString()
                textPaint.getTextBounds(id, 0, id.length, bound)
                canvas.drawText(bbox.id.toString(), rect.left.toFloat(), rect.top.toFloat() - (bound!!.height() /2), textPaint)
            }
        }

    }

    fun getColor(hash: Int): Int {
        return colors[Math.min(hash.rem(colors.size), colors.size -1)]
    }

    private fun initPool() {
        val listRect = mutableListOf<Rect>()
        for(i in 0 until 30) {
            listRect.add(Rect())
        }
        poolRect = listRect.toList()
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(
                    attrs, R.styleable.TrackFlowView, defStyleAttr, defStyleRes)
            try {
                textColor = a.getColor(R.styleable.TrackFlowView_textColor, textColor)
                rectColor = a.getColor(R.styleable.TrackFlowView_rectColor, rectColor)
            } finally {
                a.recycle()
            }
        }
    }

    private fun initPaints() {
        textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        textPaint.style = Paint.Style.FILL
        textPaint.color = textColor
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.textSize = textSize

        rectPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        rectPaint.style = Paint.Style.STROKE
        rectPaint.color = rectColor
        rectPaint.strokeWidth = lineWidth

        colors = COLORS.map {
            ContextCompat.getColor(context, it)
        }
    }

    private fun initDimensions(context: Context) {
        textSize = context.resources.pxFromSp(12)
        lineWidth = context.resources.pxFromDp(3)
    }


    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.rects = poolRect
        ss.data = data
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        poolRect = state.rects
        data = state.data
        super.onRestoreInstanceState(state.superState)
    }

    class SavedState : View.BaseSavedState {
        var rects: List<Rect> = emptyList()
        var data: Data? = null

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeTypedList(rects)
            dest.writeParcelable(data, 0)
        }

        private constructor(`in`: Parcel) : super(`in`) {
            rects = `in`.createTypedArrayList(Rect.CREATOR)
            data = `in`.readParcelable<Data>(Data::class.java.classLoader)
        }

        companion object {

            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {
        val COLORS = arrayOf(
                R.color.rect_c1, R.color.rect_c2, R.color.rect_c3, R.color.rect_c4,
                R.color.rect_c5, R.color.rect_c6, R.color.rect_c7, R.color.rect_c8)
    }
}

