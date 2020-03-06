package com.fedetto.reddit.di.modules

import com.fedetto.reddit.fragments.DetailFragment
import com.fedetto.reddit.fragments.PostsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun bindPostsFragment(): PostsFragment

    @ContributesAndroidInjector
    abstract fun bindDetailFragment(): DetailFragment
}