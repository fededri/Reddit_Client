package com.fedetto.reddit.di.modules

import com.fedetto.reddit.DetailActivity
import com.fedetto.reddit.MainActivity
import com.fedetto.reddit.di.scopes.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    @ActivityScope
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    @ActivityScope
    abstract fun detailActivity() : DetailActivity
}