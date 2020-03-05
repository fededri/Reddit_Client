package com.fedetto.reddit.utils

import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener(
    val layoutManager: RecyclerView.LayoutManager,
    private val visibleThreshold: Int = 2
) : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

    private var currentPage = 0
    private var previousTotalItemCount = 0
    private var loading = true
    private val startingPageIndex = 0

    override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val lastVisibleItemPosition = (layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()

        val totalItemCount = layoutManager.itemCount
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount)
            loading = true
        }
    }

    fun resetState() {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.loading = true
    }

    abstract fun onLoadMore(currentPage: Int, totalItemCount: Int)
}