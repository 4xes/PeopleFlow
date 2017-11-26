package io.reactivex.internal.operators.observable

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.internal.functions.ObjectHelper
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

typealias Action = () -> Unit

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.COMPUTATION)
fun <T> Observable<T>.debounceTakeFirst(windowDuration: Long, unit: TimeUnit): Observable<T> {
    return debounceTakeFirst(windowDuration, unit, Schedulers.computation())
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.CUSTOM)
fun <T> Observable<T>.debounceTakeFirst(skipDuration: Long, unit: TimeUnit, scheduler: Scheduler): Observable<T> {
    ObjectHelper.requireNonNull(unit, "unit is null")
    ObjectHelper.requireNonNull(scheduler, "scheduler is null")
    return RxJavaPlugins.onAssembly<T>(ObservableDebounceTakeFirst<T>(this, skipDuration, unit, scheduler))
}

fun <T> Observable<T>.withSchedulers(): Observable<T> {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.withSchedulers(): Single<T> {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun Completable.withSchedulers(): Completable {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}