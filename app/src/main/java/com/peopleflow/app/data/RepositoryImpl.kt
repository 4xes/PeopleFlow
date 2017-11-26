package com.peopleflow.app.data

import com.peopleflow.app.entities.LineResult
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.internal.operators.observable.withSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class RepositoryImpl  @Inject constructor(private val api: Api) : Repository {

    override fun update() = Observable.interval(500, TimeUnit.MILLISECONDS).flatMap {
        api.update()
    }.withSchedulers()

    override fun addLine(x1: Float, y1: Float, x2: Float, y2: Float): Single<LineResult> {
        return api.addLine(arrayOf(x1, y1), arrayOf(x2, y2)).withSchedulers()
    }

    override fun removeLine(id: Int): Single<LineResult> {
        return api.removeLine(id).withSchedulers()
    }
}
