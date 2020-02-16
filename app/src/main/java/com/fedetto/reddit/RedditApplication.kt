package com.fedetto.reddit


import com.fedetto.reddit.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class RedditApplication : DaggerApplication() {


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }
}