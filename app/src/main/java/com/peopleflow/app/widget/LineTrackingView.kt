package com.peopleflow.app.widget

import android.content.Context
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import com.peopleflow.app.R
import mmd.kit.ui.extension.pxFromDp
import android.view.MotionEvent
import com.peopleflow.app.entities.Line


class LineTrackingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var linePaint: Paint
    private var lineColor: Int = Color.RED
    private var lineWidth: Float = 1f
    private var line: RectF? = RectF()

    private var lines: List<Line> = emptyList()

    var listener: ((RectF) -> Unit)? = null

    init {
        initAttributes(context, attrs, defStyleAttr, 0)
        initDimensions(context)
        initPaints()
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(
                    attrs, R.styleable.LineTrackingView, defStyleAttr, defStyleRes)
            try {
                lineColor = a.getColor(R.styleable.LineTrackingView_lineColor, lineColor)
            } finally {
                a.recycle()
            }
        }
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (line == null) {
            line = RectF()
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (line != null) {
                    line!!.left = event.x - left
                    line!!.right = line!!.left
                    line!!.top= event.y - top
                    line!!.bottom = line!!.top
                }
            }
            MotionEvent.ACTION_DOWN -> {
                line!!.left = event.x - left
                line!!.right = line!!.left
                line!!.top= event.y - top
                line!!.bottom = line!!.top
            }
        }
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (line != null) {
                line!!.left = event.x - left
                line!!.right = line!!.left
                line!!.top= event.y - top
                line!!.bottom = line!!.top
            }

        } else if (event.action == MotionEvent.ACTION_UP) {
            if (line != null) {
                listener?.invoke(line!!)
                line = null
            }
        } else {
            line?.left = event.x - left
            line?.top = event.y - top
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        lines.forEach {
            canvas.drawLine(it.point1.x, it.point1.y, it.point2.x, it.point2.y, linePaint)
        }

        if (line != null) {
            canvas.drawLine(line!!.left, line!!.top, line!!.right, line!!.bottom, linePaint)
        }
    }

    fun setAddListener(listener: (RectF) -> Unit) {
        this.listener = listener
    }

    private fun initPaints() {
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        linePaint.style = Paint.Style.FILL
        linePaint.color = lineColor
        linePaint.strokeWidth = lineWidth
    }

    private fun initDimensions(context: Context) {
        lineWidth = context.resources.pxFromDp(4)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.line = line
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        line = state.line
        super.onRestoreInstanceState(state.superState)
    }


    class SavedState : View.BaseSavedState {
        var line: RectF? = null
        var lines: List<Line>? = null

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeParcelable(line, 0)
            dest.writeList(lines)
        }

        private constructor(`in`: Parcel) : super(`in`) {
            line = `in`.readParcelable(RectF::class.java.classLoader)
            `in`.readList(lines, Line::class.java.classLoader)
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

}
