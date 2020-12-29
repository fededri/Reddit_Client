package com.fedetto.reddit.viewmodels

import android.util.Log
import com.fedetto.arch.ArchViewModel
import com.fedetto.reddit.arch.*
import com.fedetto.reddit.models.RedditState
import kotlinx.coroutines.*
import javax.inject.Inject

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