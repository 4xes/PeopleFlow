package com.peopleflow.app.data


import com.peopleflow.app.entities.Data
import io.reactivex.Observable


interface Repository {
    fun update(): Observable<Data>
}
