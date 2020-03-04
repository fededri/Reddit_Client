package com.fedetto.reddit.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fedetto.reddit.Globals
import com.fedetto.reddit.controllers.RedditController
import com.fedetto.reddit.models.RedditState
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
        compositeDisposable += controller.getToken()
            .flatMap {
                Globals.ACCESS_TOKEN = it.accessToken
                controller.getPosts(10)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //TODO paint top posts
            }, Throwable::printStackTrace)
    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}