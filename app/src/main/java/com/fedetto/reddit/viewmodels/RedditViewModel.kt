package com.fedetto.reddit.viewmodels

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fedetto.reddit.Globals
import com.fedetto.reddit.controllers.RedditController
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.RedditState
import com.fedetto.reddit.views.PostItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class RedditViewModel @Inject constructor(val controller: RedditController) : ViewModel() {

    private val state = MutableLiveData<RedditState>().apply {
        value = RedditState()
    }
    private val compositeDisposable by lazy { CompositeDisposable() }

    fun observeState(): LiveData<RedditState> {
        return state
    }


    fun initialize() {
        if (shouldInitialize()) {
            emitNewState(getState().copy(isWaitingServiceResponse = true, loading = true))
            compositeDisposable += controller.getToken()
                .flatMap {
                    Globals.ACCESS_TOKEN = it.accessToken
                    controller.getPosts(50)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent { t1, t2 ->
                    emitNewState(
                        getState().copy(
                            loading = false,
                            isWaitingServiceResponse = false,
                            wasFirstRequestCalled = true
                        )
                    )
                }
                .subscribe(this::onSuccessResponse, this::onErrorResponse)
        }
    }

    private fun shouldInitialize(): Boolean {
        return state.value?.isWaitingServiceResponse == false && state.value?.wasFirstRequestCalled == false
    }


    private fun onSuccessResponse(posts: List<Post>) {
        val items = posts.map {
            PostItem(it)
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

}