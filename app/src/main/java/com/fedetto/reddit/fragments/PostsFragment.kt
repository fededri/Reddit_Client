package com.fedetto.reddit.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fedetto.reddit.R
import com.fedetto.reddit.models.RedditState
import com.fedetto.reddit.viewmodels.RedditViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PostsFragment : Fragment() {

    private val groupAdapter by lazy { GroupAdapter<GroupieViewHolder>() }
    private val recyclerView by lazy { view?.findViewById<RecyclerView>(R.id.recyclerView) }

    @Inject
    lateinit var viewModel: RedditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)

        viewModel.observeState().observe(requireActivity(), Observer {
            renderState(it)
        })

        viewModel.initialize()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    private fun initViews() {
        recyclerView?.adapter = groupAdapter
        recyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


    private fun renderState(state: RedditState) {
        state.posts?.let {
            groupAdapter.update(it)
        }
    }

    companion object {
        fun newInstance(): PostsFragment {
            return PostsFragment()
        }
    }
}