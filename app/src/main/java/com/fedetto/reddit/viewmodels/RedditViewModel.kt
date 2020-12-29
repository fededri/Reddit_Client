package com.fedetto.reddit.viewmodels

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fedetto.arch.ArchViewModel
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.arch.*
import com.fedetto.reddit.controllers.RedditController
import com.fedetto.reddit.interfaces.DispatcherProvider
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.RedditState
import com.fedetto.reddit.models.ViewAction
import com.fedetto.reddit.views.PostItem
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RedditViewModel @Inject constructor(
    private val updater: RedditUpdater,
    private val processor: RedditProcessor
) : ArchViewModel<RedditAction, RedditState, RedditSideEffect, RedditEvent>(
    updater, RedditState(),
    processor,
    coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.i("Log", "error: $throwable")
    }
) {


}