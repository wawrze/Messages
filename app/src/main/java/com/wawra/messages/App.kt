package com.wawra.messages

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.wawra.messages.di.components.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

open class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        return DaggerApplicationComponent.builder().application(this).build()
    }

    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

}