package mmd.kit.ui.extension

import android.content.res.Resources
import android.util.TypedValue

fun Resources.pxFromDp(px: Int): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px.toFloat(), displayMetrics)
}

fun Resources.pxFromSp(px: Int): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, px.toFloat(), displayMetrics)
}