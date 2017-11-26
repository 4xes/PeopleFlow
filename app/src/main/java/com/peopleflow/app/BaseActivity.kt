package com.peopleflow.app

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.observable.Action
import io.reactivex.internal.operators.observable.debounceTakeFirst
import io.reactivex.internal.operators.observable.withSchedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

abstract class BaseActivity: AppCompatActivity() {

    private val subjectAction = PublishSubject.create<(Action)>()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribe(subjectAction.debounceTakeFirst(300L, TimeUnit.MILLISECONDS).withSchedulers().subscribe({
            it.invoke()
        }, {
            it.printStackTrace()
        }))
    }

    fun subscribe(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun debounceAction(action: Action) {
        subjectAction.onNext(action)
    }

    fun replace(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(ROOT_ID, fragment, fragment.tag)
        transaction.commit()
    }

    fun replaceByClass(fragmentClass: Class<*>, bundle: Bundle? = null) {

        val fragment: Fragment?

        try {
            fragment = fragmentClass.newInstance() as Fragment
            if (bundle != null) {
                fragment.arguments = bundle
            }
            debounceAction {
                replace(fragment)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addStackByClass(fragmentClass: Class<*>, bundle: Bundle? = null) {

        val fragment: Fragment?

        try {
            fragment = fragmentClass.newInstance() as Fragment
            if (bundle != null) {
                fragment.arguments = bundle
            }
            debounceAction {
                addBackStack(fragment, fragmentClass.name)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addBackStack(fragment: Fragment, name: String) {
        supportFragmentManager.beginTransaction().add(ROOT_ID, fragment, name).addToBackStack(name).commit()
    }

    fun currentFragment() = supportFragmentManager.findFragmentById(ROOT_ID)

    fun resolveToolbar(fragment: BaseFragment) {
        if (fragment.getToolbar() != null) {
            setSupportActionBar(fragment.getToolbar())
        }

        val actionBar = supportActionBar
        if (actionBar != null) {
            if (fragment.isBack()) {
                actionBar.setDisplayHomeAsUpEnabled(true)
            }
            if (fragment.getTitle() != null) {
                actionBar.title = fragment.getTitle()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.onEach {
            if (it.isAdded && it.isVisible) {
                it.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    companion object {
        val ROOT_ID = R.id.frame
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
