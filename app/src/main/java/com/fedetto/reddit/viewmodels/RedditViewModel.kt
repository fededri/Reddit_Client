package com.fedetto.reddit.viewmodels

import com.fedetto.arch.ArchViewModel
import com.fedetto.reddit.arch.*
import com.fedetto.reddit.models.RedditState
import kotlinx.coroutines.*
import javax.inject.Inject

class RedditViewModel @Inject constructor(
    private val updater: RedditUpdater,
    private val processor: RedditProcessor,
    private val errorHandler : CoroutineExceptionHandler
) : ArchViewModel<RedditAction, RedditState, RedditSideEffect, RedditEvent>(
    updater, RedditState(),
    processor,
    coroutineExceptionHandler = errorHandler
) {


}