package com.fedetto.reddit.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.fedetto.reddit.PostBindingStrategy
import com.fedetto.reddit.R
import com.fedetto.reddit.di.factory.ViewModelFactory
import com.fedetto.reddit.models.RedditState
import com.fedetto.reddit.viewmodels.RedditViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class DetailFragment : Fragment() {


    lateinit var viewModel: RedditViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var bindingStrategy: PostBindingStrategy

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        viewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory)[RedditViewModel::class.java]

        lifecycleScope.launchWhenResumed {
            viewModel.observeState().collect {
                renderState(it)
            }
        }
        return view
    }

    private fun renderState(state: RedditState) {
        val post = state.selectedPost
        post?.let {
            bindingStrategy.bindAuthor(it, textViewAuthor)
            bindingStrategy.bindThumbnail(it, imageVieThumbnail)
            bindingStrategy.bindCreationTime(it, textViewCreationTime)
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    companion object {
        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }
}