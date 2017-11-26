package com.peopleflow.app.data.module

import dagger.Module
import dagger.Provides
import com.peopleflow.app.data.Api
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule(private val url: String) {

    @Provides
    @Singleton
    fun provideApi(builder: Retrofit.Builder): Api {
        return builder
                .baseUrl(url)
                .build()
                .create(Api::class.java)
    }

}