package com.fedetto.reddit.di.modules

import androidx.lifecycle.ViewModel
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.arch.EffectsExceptionHandler
import com.fedetto.reddit.di.PostBindingStrategyConcrete
import com.fedetto.reddit.di.scopes.ViewModelKey
import com.fedetto.reddit.interfaces.DefaultDispatcherProvider
import com.fedetto.reddit.interfaces.DispatcherProvider
import com.fedetto.reddit.viewmodels.RedditViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.CoroutineExceptionHandler

@Module(
    includes = [
        FragmentBindingModule::class,
        RedditApiModule::class,
        RoomModule::class
    ]
)
abstract class AppModule {

    @Binds
    @IntoMap
    @ViewModelKey(RedditViewModel::class)
    abstract fun bindViewModel(viewModel: RedditViewModel): ViewModel

    @Binds
    abstract fun bindingStrategy(strategyConcrete: PostBindingStrategyConcrete): PostBindingStrategy

    @Binds
    abstract fun dispatchersProvider(dispatcherProvider: DefaultDispatcherProvider): DispatcherProvider

    @Binds
    abstract fun provideEffectsErrorHandler(handler: EffectsExceptionHandler): CoroutineExceptionHandler
}