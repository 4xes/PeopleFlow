package com.peopleflow.app.entities

import android.os.Parcel
import android.os.Parcelable

data class RequstLine(
        val point1: Point,
        val point2: Point) : Parcelable {
    constructor(source: Parcel) : this(
            source.readParcelable<Point>(Point::class.java.classLoader),
            source.readParcelable<Point>(Point::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(point1, 0)
        writeParcelable(point2, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RequstLine> = object : Parcelable.Creator<RequstLine> {
            override fun createFromParcel(source: Parcel): RequstLine = RequstLine(source)
            override fun newArray(size: Int): Array<RequstLine?> = arrayOfNulls(size)
        }
    }
}