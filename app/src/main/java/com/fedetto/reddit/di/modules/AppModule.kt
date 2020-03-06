package com.fedetto.reddit.di.modules

import androidx.lifecycle.ViewModel
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.di.PostBindingStrategyConcrete
import com.fedetto.reddit.di.scopes.ViewModelKey
import com.fedetto.reddit.viewmodels.RedditViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(
    includes = [
        FragmentBindingModule::class,
        RedditApiModule::class
    ]
)
abstract class AppModule {

    @Binds
    @IntoMap
    @ViewModelKey(RedditViewModel::class)
    abstract fun bindViewModel(viewModel: RedditViewModel): ViewModel

    @Binds
    abstract fun bindingStrategy(strategyConcrete: PostBindingStrategyConcrete): PostBindingStrategy
}