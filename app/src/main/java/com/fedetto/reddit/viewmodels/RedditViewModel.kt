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

    private val state = MutableLiveData<RedditState>()
    private val compositeDisposable by lazy { CompositeDisposable() }

    fun observeState(): LiveData<RedditState> {
        return state
    }


    fun initialize() {
        state.value = RedditState()
        showLoading()
        compositeDisposable += controller.getToken()
            .flatMap {
                Globals.ACCESS_TOKEN = it.accessToken
                controller.getPosts(50)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onSuccessResponse, this::onErrorResponse)
    }


    private fun onSuccessResponse(posts: List<Post>) {
        hideLoading()
        val items = posts.map {
            PostItem(it)
        }
        state.postValue(state.value?.copy(posts = items))
    }

    private fun onErrorResponse(throwable: Throwable) {
        //show error message
        hideLoading()
        throwable.printStackTrace()
    }

    @MainThread
    private fun showLoading() {
        state.value = state.value?.copy(loading = true)
    }

    @MainThread
    private fun hideLoading() {
        state.value = state.value?.copy(loading = false)
    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}