package com.peopleflow.app.widget

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.peopleflow.app.entities.Data

open class LineslView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    val linesAdapter = LinesAdapter()

    fun setListener(listener: (Int) -> Unit) {
        linesAdapter.listener = listener
    }

    fun setData(data: Data) {
        linesAdapter.models = data.line
    }

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = linesAdapter
    }

}