package com.fedetto.reddit

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fedetto.reddit.di.factory.ViewModelFactory
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.RedditState
import com.fedetto.reddit.models.ViewAction
import com.fedetto.reddit.viewmodels.RedditViewModel
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: RedditViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var isLandscape = false

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onResume() {
        AndroidInjection.inject(this)
        super.onResume()

        viewModel = ViewModelProviders.of(this, viewModelFactory)[RedditViewModel::class.java]
        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        observeViewActions()
    }

    private fun observeViewActions() {
        compositeDisposable += viewModel.getViewActionsObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                when (it) {
                    is ViewAction.SelectPost -> onPostSelected(it.post)
                }
            }, Throwable::printStackTrace)
    }

    private fun onPostSelected(post: Post) {
        if (!isLandscape) {
            startActivity(Intent(this, DetailActivity::class.java).apply {
                putExtra("post", post)
            })
        }
    }


    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
}
