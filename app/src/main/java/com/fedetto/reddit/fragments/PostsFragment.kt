package com.fedetto.reddit.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fedetto.reddit.R
import com.fedetto.reddit.arch.RedditAction
import com.fedetto.reddit.di.factory.ViewModelFactory
import com.fedetto.reddit.models.RedditState
import com.fedetto.reddit.utils.EndlessRecyclerViewScrollListener
import com.fedetto.reddit.viewmodels.RedditViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class PostsFragment : Fragment() {

    private val groupAdapter by lazy { GroupAdapter<GroupieViewHolder>() }
    private val recyclerView by lazy { view?.findViewById<RecyclerView>(R.id.recyclerView) }
    private val progressBar by lazy { view?.findViewById<ProgressBar>(R.id.progressBar) }
    private val pullToRefresh by lazy { view?.findViewById<SwipeRefreshLayout>(R.id.pullToRefresh) }
    private val buttonDismissAll by lazy { view?.findViewById<Button>(R.id.buttonDismissAll) }


    lateinit var viewModel: RedditViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory)[RedditViewModel::class.java]

        viewModel.action(RedditAction.AppInitiated(viewModel))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        lifecycleScope.launchWhenResumed {
            viewModel.observeState().collect {
                renderState(it)
            }
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    private fun initViews() {
        recyclerView?.adapter = groupAdapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = layoutManager


        recyclerView?.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(currentPage: Int, totalItemCount: Int) {
                viewModel.action(RedditAction.LoadMorePosts(viewModel))
            }
        })

        pullToRefresh?.setOnRefreshListener {
            viewModel.action(RedditAction.Refresh(viewModel))
        }

        buttonDismissAll?.setOnClickListener {
            viewModel.action(RedditAction.DismissAll)
        }
    }

    private fun renderState(state: RedditState) {
        state.posts?.let {

            recyclerView?.visibility = View.VISIBLE
            groupAdapter.update(it)

        }

        progressBar?.visibility = if (state.loading) View.VISIBLE else View.GONE
        pullToRefresh?.isRefreshing = state.isRefreshing
    }

    private inline fun playDismissAnimation(crossinline callback: () -> Unit) {
        val scaleY = ObjectAnimator.ofFloat(recyclerView, View.SCALE_Y, 1f, 0f)
        val scaleX = ObjectAnimator.ofFloat(recyclerView, View.SCALE_X, 1f, 0f)
        val rotation = ObjectAnimator.ofFloat(recyclerView, View.ROTATION, 0f, 90f)
        val alpha = ObjectAnimator.ofFloat(recyclerView, View.ALPHA, 1f, 0f)

        val set = AnimatorSet().apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 1000
            playTogether(scaleX, scaleY, rotation, alpha)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    callback.invoke()
                }
            })
        }

        set.start()
    }

    companion object {
        fun newInstance(): PostsFragment {
            return PostsFragment()
        }
    }
}