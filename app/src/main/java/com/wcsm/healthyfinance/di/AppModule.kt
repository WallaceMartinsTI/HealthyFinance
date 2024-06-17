package com.wcsm.healthyfinance.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wcsm.healthyfinance.data.repository.BillRepository
import com.wcsm.healthyfinance.data.repository.BillRepositoryImpl
import com.wcsm.healthyfinance.data.repository.UserRepository
import com.wcsm.healthyfinance.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideUserRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideBillRepository(
        firestore: FirebaseFirestore
    ): BillRepository {
        return BillRepositoryImpl(firestore)
    }

}