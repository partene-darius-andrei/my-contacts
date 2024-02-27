package com.my.contacts.di.modules

import android.content.Context
import com.my.contacts.utils.NetworkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideNetworkHelper(@ApplicationContext context: Context) =
        NetworkHelper(context)

}