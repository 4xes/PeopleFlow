package com.peopleflow.app.data

import com.peopleflow.app.entities.Data
import io.reactivex.Observable
import retrofit2.http.POST


interface Api {

//    @FormUrlEncoded
//    @POST("/api/add_line")
//    fun sendCode(@Field("code") code: String): Single<CodeResponse>

    @POST("/api/update")
    fun update(): Observable<Data>
}
