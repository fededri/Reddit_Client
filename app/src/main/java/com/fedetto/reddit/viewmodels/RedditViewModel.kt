package com.fedetto.reddit.viewmodels

import com.fedetto.arch.ArchViewModel
import com.fedetto.reddit.arch.*
import com.fedetto.reddit.models.RedditState
import kotlinx.coroutines.*
import javax.inject.Inject

class RedditViewModel @Inject constructor(
    private val updater: RedditUpdater,
    private val processor: RedditProcessor,
    private val errorHandler: CoroutineExceptionHandler,
    private val stateMapper: RedditStateMapper
) : ArchViewModel<RedditAction, RedditState, RedditSideEffect, RedditEvent, RenderState>(
    updater, RedditState(), setOf(RedditSideEffect.LoadPosts),
    stateMapper = stateMapper,
    processor = processor,
    coroutineExceptionHandler = errorHandler
) {


}