package com.peopleflow.app.data


import com.peopleflow.app.entities.Data
import com.peopleflow.app.entities.LineResult
import io.reactivex.Observable
import io.reactivex.Single


interface Repository {
    fun update(): Observable<Data>

    fun addLine(x1: Float, y1: Float, x2: Float, y2: Float): Single<LineResult>

    fun removeLine(id: Int): Single<LineResult>
}
