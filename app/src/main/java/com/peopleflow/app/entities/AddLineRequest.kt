package com.peopleflow.app.entities

import android.os.Parcel
import android.os.Parcelable

data class AddLineRequest(val line: RequstLine) : Parcelable {
    constructor(source: Parcel) : this(
            source.readParcelable<RequstLine>(RequstLine::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(line, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<AddLineRequest> = object : Parcelable.Creator<AddLineRequest> {
            override fun createFromParcel(source: Parcel): AddLineRequest = AddLineRequest(source)
            override fun newArray(size: Int): Array<AddLineRequest?> = arrayOfNulls(size)
        }
    }
}
