package com.peopleflow.app.data

import com.peopleflow.app.entities.AddLineRequest
import com.peopleflow.app.entities.Data
import com.peopleflow.app.entities.LineResult
import io.reactivex.Observable
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface Api {

    @POST("/api/update")
    fun update(): Observable<Data>

    @POST("/api/add_line")
    fun addLine(@Body body: AddLineRequest): Single<LineResult>

    @FormUrlEncoded
    @POST("/api/removeLine")
    fun removeLine(@Field("id") id: Int): Single<LineResult>
}
