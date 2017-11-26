package com.peopleflow.app.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import com.peopleflow.app.R

open class AspectRatioImageView : AppCompatImageView {

    protected var aspectRatio: Float = ASPECT_DEFAULT
    protected var mode = Mode.NONE

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        if (attrs == null || context == null) {
            return
        }
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.AspectRatioImageView, defStyleAttr, defStyleAttr)
        try {
            val modeInt = a.getInt(R.styleable.AspectRatioImageView_aspectMode, 0)
            when (modeInt) {
                Mode.SQUARE.code -> {
                    mode = Mode.SQUARE
                }
                Mode.CINEMA.code -> {
                    mode = Mode.CINEMA
                }
                Mode.CUSTOM.code -> {
                    mode = Mode.CUSTOM
                    aspectRatio =
                            a.getFloat(R.styleable.AspectRatioImageView_aspect_height, 1f) /
                                    a.getFloat(R.styleable.AspectRatioImageView_aspect_width, 1f)
                }
                else -> {
                    mode = Mode.NONE
                }
            }
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val newWidth: Int
        val newHeight: Int
        when (mode) {
            Mode.SQUARE -> {
                newWidth = measuredWidth
                newHeight = measuredWidth
            }
            Mode.CINEMA -> {
                newWidth = measuredWidth
                newHeight = (measuredWidth.toFloat() * ASPECT_CINEMA).toInt()
            }
            Mode.CUSTOM -> {
                newWidth = measuredWidth
                newHeight = (measuredWidth.toFloat() * aspectRatio).toInt()
            }
            else -> {
                return
            }
        }

        super.onMeasure(View.MeasureSpec.makeMeasureSpec(newWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(newHeight, View.MeasureSpec.EXACTLY))
    }

    fun setAspect(aspectRatio: Float) {
        if (this.aspectRatio != aspectRatio) {
            this.aspectRatio = aspectRatio
            this.mode = Mode.CUSTOM
            requestLayout()
        }
    }

    fun setAspectMode(mode: Mode) {
        if (this.mode != mode) {
            this.mode = mode
            requestLayout()
        }
    }

    enum class Mode(var code: Int) {
        NONE(0),
        CUSTOM(1),
        SQUARE(2),
        CINEMA(3)
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState())
        bundle.putFloat(STATE_ASPECT, aspectRatio)
        bundle.putString(STATE_MODE, mode.name)
        return bundle
    }

    @SuppressLint("MissingSuperCall")
    override fun onRestoreInstanceState(state: Parcelable?) {

        if (state != null && state is Bundle) {
            mode = Mode.valueOf(state.getString(STATE_MODE))
            aspectRatio = state.getFloat(STATE_ASPECT)
            super.onRestoreInstanceState(state.getParcelable(STATE_SUPER))
            return
        }
        super.onRestoreInstanceState(state)
    }

    companion object {
        private const val STATE_MODE = "mmd.kit.ui.widget.State.Mode"
        private const val STATE_ASPECT = "mmd.kit.ui.widget.State.Aspect"
        private const val STATE_SUPER = "mmd.kit.ui.widget.State.Super"
        private const val ASPECT_DEFAULT = 1f
        private const val ASPECT_CINEMA = 9f / 16f
    }

}
