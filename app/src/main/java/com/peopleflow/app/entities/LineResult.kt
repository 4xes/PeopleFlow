package com.peopleflow.app.entities

import android.os.Parcel
import android.os.Parcelable

data class LineResult(val id: Int) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LineResult> = object : Parcelable.Creator<LineResult> {
            override fun createFromParcel(source: Parcel): LineResult = LineResult(source)
            override fun newArray(size: Int): Array<LineResult?> = arrayOfNulls(size)
        }
    }
}

