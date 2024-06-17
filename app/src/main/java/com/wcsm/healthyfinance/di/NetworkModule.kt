package com.wcsm.healthyfinance.di

import android.content.Context
import android.net.ConnectivityManager
import com.wcsm.healthyfinance.data.repository.NetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideNetworkRepository(connectivityManager: ConnectivityManager): NetworkRepository {
        return NetworkRepository(connectivityManager)
    }

}