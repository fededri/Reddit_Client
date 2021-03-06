package com.fedetto.reddit.viewmodels

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.controllers.RedditController
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.RedditState
import com.fedetto.reddit.models.ViewAction
import com.fedetto.reddit.views.PostItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class RedditViewModel @Inject constructor(
    private val controller: RedditController,
    private val bindingStrategy: PostBindingStrategy
) : ViewModel() {

    private val state = MutableLiveData<RedditState>().apply {
        value = RedditState()
    }
    private val compositeDisposable by lazy { CompositeDisposable() }
    private val pageSize = 10
    private val viewActions = PublishSubject.create<ViewAction>()


    fun observeState(): LiveData<RedditState> {
        return state
    }

    fun getViewActionsObservable(): Observable<ViewAction> {
        return viewActions.hide()
    }


    fun initialize() {
        if (shouldInitialize()) {
            observeViewActions()
            emitNewState(getState().copy(isWaitingServiceResponse = true, loading = true))
            fetchPosts()
        }
    }

    private fun observeViewActions() {
        compositeDisposable += viewActions.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::processViewAction, Throwable::printStackTrace)
    }

    private fun processViewAction(action: ViewAction) {
        when (action) {
            is ViewAction.SelectPost -> emitNewState(getState().copy(selectedPost = action.post))
            is ViewAction.DismissPost -> onDismissPostClick(action.post)
        }
    }

    private fun fetchPosts() {
        compositeDisposable += controller.getPosts(pageSize)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnEvent { t1, t2 ->
                emitNewState(
                    getState().copy(
                        loading = false,
                        isWaitingServiceResponse = false,
                        wasFirstRequestCalled = true,
                        isRefreshing = false
                    )
                )
            }
            .subscribe(this::onSuccessResponse, this::onErrorResponse)
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
        super.onCleared()
    }

    fun refresh() {
        getState().copy(isRefreshing = true)
        fetchPosts()
    }

    fun loadMore() {
        emitNewState(getState().copy(loading = true, isWaitingServiceResponse = true))
        compositeDisposable += controller.loadMore(pageSize)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnEvent { t1, t2 ->
                emitNewState(
                    getState().copy(
                        loading = false,
                        isWaitingServiceResponse = false
                    )
                )
            }
            .subscribe(this::updateNewPosts, this::onErrorResponse)
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

    private fun onDismissPostClick(item: PostItem) {
        val currentItems = getState().posts?.toMutableList()
        currentItems?.remove(item)
        emitNewState(
            getState().copy(
                posts = currentItems
            )
        )
    }

    fun dismissAll() {
        emitNewState(
            getState().copy(posts = listOf())
        )
    }

}