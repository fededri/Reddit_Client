package com.fedetto.reddit.di

import android.app.Application
import com.fedetto.reddit.RedditApplication
import com.fedetto.reddit.di.factory.FactoryModule
import com.fedetto.reddit.di.modules.ActivityBindingModule
import com.fedetto.reddit.di.modules.AppModule
import com.fedetto.reddit.di.scopes.AppScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityBindingModule::class,
        FactoryModule::class
    ]
)
@AppScope
interface AppComponent : AndroidInjector<RedditApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}