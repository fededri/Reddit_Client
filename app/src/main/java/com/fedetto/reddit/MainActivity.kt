package com.fedetto.reddit

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.fedetto.reddit.arch.RedditEvent
import com.fedetto.reddit.di.factory.ViewModelFactory
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.viewmodels.RedditViewModel
import dagger.android.AndroidInjection
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: RedditViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var isLandscape = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launchWhenResumed {
            viewModel.observeEvents().collect {
                processEvent(it)
            }
        }
    }

    override fun onResume() {
        AndroidInjection.inject(this)
        super.onResume()

        viewModel = ViewModelProviders.of(this, viewModelFactory)[RedditViewModel::class.java]
        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    private fun processEvent(event: RedditEvent) {
        when (event) {
            is RedditEvent.OnPostSelected -> onPostSelected(event.post)
        }
    }

    private fun onPostSelected(post: Post) {
        if (!isLandscape) {
            startActivity(Intent(this, DetailActivity::class.java).apply {
                putExtra("post", post)
            })
        }
    }
}
