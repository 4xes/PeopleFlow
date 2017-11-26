package mmd.kit.ui.extension

import android.support.v4.app.Fragment
import android.widget.Toast

fun Fragment.toast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun Fragment.toast(message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

