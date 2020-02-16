package com.fedetto.reddit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fedetto.reddit.controllers.RedditController
import com.fedetto.reddit.models.RedditState
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RedditViewModel @Inject constructor(val controller: RedditController) : ViewModel() {

    private val state = MutableLiveData<RedditState>()
    private val compositeDisposable by lazy { CompositeDisposable() }

    fun observeState(): LiveData<RedditState> {
        return state
    }


    fun initialize() {

    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}