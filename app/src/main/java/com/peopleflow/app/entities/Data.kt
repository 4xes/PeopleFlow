package com.peopleflow.app.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Data(val frame_path: String?, val bbox: List<Bbox>?, val lines: List<Line>?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.createTypedArrayList(Bbox.CREATOR),
            ArrayList<Line>().apply { source.readList(this, Line::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(frame_path)
        writeTypedList(bbox)
        writeList(lines)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Data> = object : Parcelable.Creator<Data> {
            override fun createFromParcel(source: Parcel): Data = Data(source)
            override fun newArray(size: Int): Array<Data?> = arrayOfNulls(size)
        }
    }
}
