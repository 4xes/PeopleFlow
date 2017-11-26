package com.peopleflow.app

import android.os.Bundle
import android.util.Log
import com.peopleflow.app.annotations.Layout
import com.peopleflow.app.annotations.Title
import com.peopleflow.app.data.Repository
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_flow.*
import javax.inject.Inject

@Title(R.string.title_flow)
@Layout(R.layout.fragment_flow)
class FlowFragment: BaseFragment() {

    var disposable: Disposable? = null

    @Inject
    lateinit var repository: Repository

    private val TAG = this.javaClass.simpleName!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()

        disposable = repository.update().subscribe({
            Log.d(TAG, it.toString())
            flow.data = it
        }, {
            it.printStackTrace()
        })
    }

    override fun onPause() {
        super.onPause()

        disposable?.dispose()
        disposable = null
    }
}
