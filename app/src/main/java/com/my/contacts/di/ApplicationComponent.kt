package com.my.contacts.di

import com.my.contacts.di.modules.NetworkModule
import com.my.contacts.di.modules.RoomModule
import com.my.contacts.di.modules.RetrofitModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class, RoomModule::class, NetworkModule::class])
interface ApplicationComponent
