package com.fedetto.reddit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fedetto.reddit.controllers.RedditController
import com.fedetto.reddit.models.Post
import com.fedetto.reddit.models.ViewAction
import com.fedetto.reddit.viewmodels.RedditViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RedditViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var controller: RedditController

    @Mock
    lateinit var bindingStrategy: PostBindingStrategy

    private lateinit var viewModel: RedditViewModel

    @Before
    fun setup() {
        viewModel = RedditViewModel(controller, bindingStrategy, coroutinesTestRule.testDispatcherProvider)
    }


    @Test
    fun `fetch one post - assert one post is saved to the state list`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val post = mock<Post>()
        whenever(controller.getPosts(any())).doReturn(listOf(post))

        viewModel.initialize()
        val state = viewModel.observeState().value
        assert(state?.posts?.size == 1)
    }

    @Test
    fun `dismiss click - remove item from list`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val post = mock<Post>()
        whenever(controller.getPosts(any())).doReturn(listOf(post))

        viewModel.initialize()
        viewModel.viewActions.send(ViewAction.DismissPost(post))
        val state = viewModel.observeState().value
        assert(state?.posts?.isEmpty() == true)
    }
}