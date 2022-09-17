package com.kostry.yourtimer.di.module

import android.app.Application
import com.kostry.yourtimer.datasource.DatasourceRepository
import com.kostry.yourtimer.datasource.DatasourceRepositoryImpl
import com.kostry.yourtimer.datasource.database.AppDatabase
import com.kostry.yourtimer.util.MyTimer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideApplication(): Application {
        return application
    }

    @Singleton
    @Provides
    fun provideMyTimer(): MyTimer = MyTimer()

    @Singleton
    @Provides
    fun provideDatasourceRepository(
        appDatabase: AppDatabase,
    ): DatasourceRepository {
        return DatasourceRepositoryImpl(appDatabase)
    }
}