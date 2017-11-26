package com.peopleflow.app

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import com.peopleflow.app.annotations.Layout
import com.peopleflow.app.annotations.Title
import com.peopleflow.app.data.Repository
import com.peopleflow.app.entities.Data
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_flow.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPOutputStream
import javax.inject.Inject



@Title(R.string.title_flow)
@Layout(R.layout.fragment_flow)
class FlowFragment: BaseFragment() {

    private var disposable: Disposable? = null
    var data: Data? = null

    @Inject
    lateinit var repository: Repository

    private val TAG = this.javaClass.simpleName!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        data = savedInstanceState?.getParcelable("data")
        if (data != null) {
            setResult(data!!)
        }
    }

    override fun onResume() {
        super.onResume()
        start()
    }

    fun start() {
        flow?.data = null
        disposable = repository.update().subscribe({
            setResult(it)
        }, {
            it.printStackTrace()
            subscribe(Observable.timer(300, TimeUnit.MILLISECONDS).subscribe {
                start()
            })
        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable("data", data)
    }

    fun setResult(data: Data) {
        Log.d(TAG, data.toString())
        if (data.frame_path != null) {
            Picasso.with(context).load(data.frame_path).noPlaceholder().noFade().into(object: Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {

                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    progressWrapper?.apply {
                        if (visibility == View.VISIBLE) {
                            visibility = View.GONE
                        }
                    }
                    setUpdateImage(bitmap)
                    flow?.data = data
                }
            })
        }
    }
    fun setUpdateImage(bitmap: Bitmap?) {
        if (bitmap != null) {
            val width = bitmap.width
            val height = bitmap.height
            stream?.setAspect(height.toFloat() / width.toFloat())
            stream?.setImageBitmap(bitmap)
        }
    }


    override fun onPause() {
        super.onPause()

        if (disposable != null && !disposable!!.isDisposed) {
            disposable?.dispose()
            disposable = null
        }
    }

    companion object {
        var bitmap: Bitmap? = null
    }
}
