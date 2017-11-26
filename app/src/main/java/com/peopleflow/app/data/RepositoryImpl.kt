package com.peopleflow.app.data

import io.reactivex.Observable
import io.reactivex.internal.operators.observable.withSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class RepositoryImpl  @Inject constructor(private val api: Api) : Repository {

    override fun update() = Observable.interval(3000, TimeUnit.MILLISECONDS).flatMap {
        api.update()
    }.withSchedulers()
}
