package com.my.contacts.di

import com.my.contacts.di.modules.OtherModule
import com.my.contacts.di.modules.RetrofitModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class, OtherModule::class])
interface ApplicationComponent

