package com.fedetto.reddit.di.modules

import com.fedetto.reddit.controllers.RedditController
import com.fedetto.reddit.di.scopes.ActivityScope
import com.fedetto.reddit.di.scopes.ViewModelKey
import com.fedetto.reddit.viewmodels.RedditViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(
    includes = [
        FragmentBindingModule::class,
        RedditApiModule::class
        ]
)
class AppModule {

    @Provides
    @ActivityScope
    @IntoMap
    @ViewModelKey(RedditViewModel::class)
    fun bindViewModel(controller: RedditController): RedditViewModel {
        return RedditViewModel(controller)
    }
}