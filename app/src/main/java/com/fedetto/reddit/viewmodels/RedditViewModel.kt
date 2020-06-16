package com.fedetto.reddit.viewmodels

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.controllers.RedditController
import com.fedetto.reddit.interfaces.DispatcherProvider
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.RedditState
import com.fedetto.reddit.models.ViewAction
import com.fedetto.reddit.views.PostItem
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RedditViewModel @Inject constructor(
    private val controller: RedditController,
    private val bindingStrategy: PostBindingStrategy,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val state = MutableLiveData<RedditState>()
    private val compositeDisposable by lazy { CompositeDisposable() }
    private val pageSize = 10
    @VisibleForTesting
    val viewActions = BroadcastChannel<ViewAction>(1)
    private val receiveChannels = mutableListOf<ReceiveChannel<*>>()


    fun observeState(): LiveData<RedditState> {
        return state
    }

    fun getViewActionsObservable(): ReceiveChannel<ViewAction> {
        return viewActions.openSubscription()
    }


    fun initialize() {
        state.value = RedditState()
        if (shouldInitialize()) {
            observeViewActions()
            emitNewState(getState().copy(isWaitingServiceResponse = true, loading = true))
            fetchPosts()
        }
    }

    private fun observeViewActions() {
        viewModelScope.launch(dispatcherProvider.main()) {
            val subscription = viewActions.openSubscription()
            receiveChannels.add(subscription)
            for (action in subscription) {
                processViewAction(action)
            }
        }
    }

    private fun processViewAction(action: ViewAction) {
        when (action) {
            is ViewAction.SelectPost -> emitNewState(getState().copy(selectedPost = action.post))
            is ViewAction.DismissPost -> onDismissPostClick(action.post)
        }
    }

    private fun fetchPosts() {
        CoroutineScope(dispatcherProvider.io()).launch {
            val posts = controller.getPosts(pageSize)
            viewModelScope.launch {
                emitNewState(
                    getState().copy(
                        loading = false,
                        isWaitingServiceResponse = false,
                        wasFirstRequestCalled = true,
                        isRefreshing = false
                    )
                )
                onSuccessResponse(posts)
            }
        }
    }


    private fun shouldInitialize(): Boolean {
        return state.value?.isWaitingServiceResponse == false && state.value?.wasFirstRequestCalled == false
    }


    private fun onSuccessResponse(posts: List<Post>) {
        val items = posts.map {
            PostItem(it, viewActions, bindingStrategy)
        }
        emitNewStateAsync(getState().copy(posts = items))
    }

    private fun onErrorResponse(throwable: Throwable) {
        //show error message
        throwable.printStackTrace()
    }

    @MainThread
    private fun emitNewState(newState: RedditState) {
        state.value = newState
    }

    private fun emitNewStateAsync(newState: RedditState) {
        state.postValue(newState)
    }

    private fun getState() = state.value ?: RedditState()


    override fun onCleared() {
        compositeDisposable.clear()
        receiveChannels.forEach {
            it.cancel()
        }
        super.onCleared()
    }

    fun refresh() {
        getState().copy(isRefreshing = true)
        fetchPosts()
    }

    fun loadMore() {
        emitNewState(getState().copy(loading = true, isWaitingServiceResponse = true))
        CoroutineScope(dispatcherProvider.io()).launch {
            val posts = controller.loadMore(pageSize)
            viewModelScope.launch {
                val threadName = Thread.currentThread().name
                Log.i("Controller", "running on thread $threadName")
                emitNewState(
                    getState().copy(
                        loading = false,
                        isWaitingServiceResponse = false
                    )
                )
                updateNewPosts(posts)
            }
        }
    }

    private fun updateNewPosts(newPosts: List<Post>) {
        val newItems = newPosts.map {
            PostItem(it, viewActions, bindingStrategy)
        }

        val allItems = getState().posts?.toMutableList()?.apply {
            addAll(newItems)
        }
        emitNewStateAsync(
            getState().copy(
                posts = allItems
            )
        )
    }

    private fun onDismissPostClick(post: Post) {
        val newItems = getState().posts?.filter {
            val item = it as? PostItem
            item?.post != post
        }

        emitNewState(
            getState().copy(
                posts = newItems
            )
        )
    }

    fun dismissAll() {
        emitNewState(
            getState().copy(posts = listOf())
        )
    }

}