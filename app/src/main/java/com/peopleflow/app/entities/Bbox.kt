package com.peopleflow.app.entities

import android.os.Parcel
import android.os.Parcelable

data class Bbox(
        val id: Int,
        val point1: Point,
        val point2: Point
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readParcelable<Point>(Point::class.java.classLoader),
            source.readParcelable<Point>(Point::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeParcelable(point1, 0)
        writeParcelable(point2, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Bbox> = object : Parcelable.Creator<Bbox> {
            override fun createFromParcel(source: Parcel): Bbox = Bbox(source)
            override fun newArray(size: Int): Array<Bbox?> = arrayOfNulls(size)
        }
    }
}