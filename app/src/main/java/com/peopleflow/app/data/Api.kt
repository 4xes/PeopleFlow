package com.peopleflow.app.data

import com.peopleflow.app.entities.Data
import com.peopleflow.app.entities.LineResult
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface Api {

    @POST("/api/update")
    fun update(): Observable<Data>

    @FormUrlEncoded
    @POST("/api/add_line")
    fun addLine(@Field("point1") point1: Array<Float>, @Field("point2") point2: Array<Float>): Single<LineResult>

    @FormUrlEncoded
    @POST("/api/removeLine")
    fun removeLine(@Field("id") id: Int): Single<LineResult>
}
