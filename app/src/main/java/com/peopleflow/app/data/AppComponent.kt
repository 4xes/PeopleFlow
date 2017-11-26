package com.peopleflow.app.data

import android.app.Application
import com.peopleflow.app.FlowFragment
import dagger.Component
import com.peopleflow.app.data.module.ApiModule
import com.peopleflow.app.data.module.AppModule
import com.peopleflow.app.data.module.NetworkModule
import com.peopleflow.app.data.module.RepositoryModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(RepositoryModule::class, AppModule::class, NetworkModule::class, ApiModule::class))
interface AppComponent {

    fun inject(flowFragment: FlowFragment)

    companion object Factory {

        fun create(app: Application) = DaggerAppComponent.builder().
                appModule(AppModule(app)).
                apiModule(ApiModule("http://home.totruok.ru:55514/")).
                networkModule(NetworkModule()).
                repositoryModule(RepositoryModule()).
                build()
    }
}
