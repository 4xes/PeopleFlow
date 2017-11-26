package com.peopleflow.app.entities

import android.os.Parcel
import android.os.Parcelable

data class Point(val x: Float, val y: Float) : Parcelable {
    constructor(source: Parcel) : this(
            source.readFloat(),
            source.readFloat()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeFloat(x)
        writeFloat(y)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Point> = object : Parcelable.Creator<Point> {
            override fun createFromParcel(source: Parcel): Point = Point(source)
            override fun newArray(size: Int): Array<Point?> = arrayOfNulls(size)
        }
    }
}