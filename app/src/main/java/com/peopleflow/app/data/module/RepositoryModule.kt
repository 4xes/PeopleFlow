package com.peopleflow.app.data.module

import dagger.Module
import dagger.Provides
import com.peopleflow.app.data.Api
import com.peopleflow.app.data.Repository
import com.peopleflow.app.data.RepositoryImpl
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(api: Api): Repository {
        return RepositoryImpl(api)

    }
}
