package com.peopleflow.app

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.peopleflow.app.annotations.Back
import com.peopleflow.app.annotations.Layout
import com.peopleflow.app.annotations.Title
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.observable.Action
import io.reactivex.internal.operators.observable.debounceTakeFirst
import io.reactivex.internal.operators.observable.withSchedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

open class BaseFragment: Fragment() {

    private val subjectAction = PublishSubject.create<(Action)>()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    open val nameScreen: String
        get() = this.javaClass.simpleName!!

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(getLayoutId(), container, false)

        subscribe(subjectAction.debounceTakeFirst(300L, TimeUnit.MILLISECONDS).withSchedulers().subscribe({
            it.invoke()
        }, {
            it.printStackTrace()
        }))

        return view
    }

    fun subscribe(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun debounceAction(action: Action) {
        subjectAction.onNext(action)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as BaseActivity).resolveToolbar(this)
    }

    fun getToolbar(): Toolbar? {
        return view?.findViewById(R.id.toolbar)
    }

    fun getLayoutId(): Int {
        val layoutAnnotation = this.javaClass.annotations.find { it.annotationClass == Layout::class } as? Layout ?:
                throw IllegalStateException("Please override getLayoutId() or set @Layout annotation to Fragment")
        return layoutAnnotation.layout
    }

    open fun getTitle(): String? {
        val titleAnnotation = javaClass.annotations.find { it.annotationClass == Title::class } as? Title ?:
                throw IllegalStateException("Please override getString() or set @Title annotation to Fragment")
        return getString(titleAnnotation.title)
    }

    fun isBack(): Boolean {
        return javaClass.isAnnotationPresent(Back::class.java)
    }
}
