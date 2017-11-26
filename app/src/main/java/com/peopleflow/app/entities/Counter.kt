package com.peopleflow.app.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Counter(
        @SerializedName("in")
        val inFlow: Int,
        @SerializedName("out")
        val outFlow: Int) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(inFlow)
        writeInt(outFlow)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Counter> = object : Parcelable.Creator<Counter> {
            override fun createFromParcel(source: Parcel): Counter = Counter(source)
            override fun newArray(size: Int): Array<Counter?> = arrayOfNulls(size)
        }
    }
}