package com.peopleflow.app.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import com.peopleflow.app.R
import mmd.kit.ui.extension.pxFromDp

class LineTrackingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var linePaint: Paint
    private var lineColor: Int = Color.RED
    private var lineWidth: Float = 1f
    private var line = Rect()

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

    private fun initPaints() {
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
        linePaint.style = Paint.Style.FILL
        linePaint.color = lineColor
    }

    private fun initDimensions(context: Context) {
        lineWidth = context.resources.pxFromDp(5)
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
        var line = Rect()


        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeParcelable(line, 0)
        }

        private constructor(`in`: Parcel) : super(`in`) {
            line = `in`.readParcelable<Rect>(Rect::class.java.classLoader)
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
