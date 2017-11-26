package com.peopleflow.app.data

import com.peopleflow.app.entities.AddLineRequest
import com.peopleflow.app.entities.LineResult
import com.peopleflow.app.entities.Point
import com.peopleflow.app.entities.RequstLine
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.internal.operators.observable.withSchedulers
import okhttp3.internal.http.RequestLine
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class RepositoryImpl  @Inject constructor(private val api: Api) : Repository {

    override fun update() = Observable.interval(500, TimeUnit.MILLISECONDS).flatMap {
        api.update()
    }.withSchedulers()

    override fun addLine(x1: Float, y1: Float, x2: Float, y2: Float): Single<LineResult> {
        val jsonObject = JSONObject()
        val arrayPoint1 = JSONObject()
        arrayPoint1.put("x", x1)
        arrayPoint1.put("y", y1)
        jsonObject.put("point1", arrayPoint1)
        val arrayPoint2 = JSONObject()
        arrayPoint2.put("x", x2)
        arrayPoint2.put("y", y2)
        jsonObject.put("point2", arrayPoint2)
        return api.addLine(AddLineRequest(RequstLine(Point(x1, y1), Point(x2, y2)))).withSchedulers()
    }

    override fun removeLine(id: Int): Single<LineResult> {
        return api.removeLine(id).withSchedulers()
    }
}
