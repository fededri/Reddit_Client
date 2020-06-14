package com.fedetto.reddit

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: RedditViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var isLandscape = false

    private val compositeDisposable = CompositeDisposable()
    private var receiveChannel: ReceiveChannel<ViewAction>? = null

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
        lifecycleScope.launch {
            receiveChannel = viewModel.getViewActionsObservable()
            receiveChannel?.let {
                for (action in it) {
                    when (action) {
                        is ViewAction.SelectPost -> onPostSelected(action.post)
                    }
                }
            }

        }
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
        receiveChannel?.cancel()
        super.onStop()
    }
}
