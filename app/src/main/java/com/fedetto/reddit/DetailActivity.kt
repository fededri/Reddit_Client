package com.fedetto.reddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fedetto.reddit.models.Post
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    @Inject
    lateinit var bindingStrategy: PostBindingStrategy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_detail)
    }


    override fun onResume() {
        AndroidInjection.inject(this)
        super.onResume()

        bindPost()
    }

    private fun bindPost(){
        val post = intent.extras?.getParcelable("post") as? Post
        post?.let {
            bindingStrategy.bindAuthor(it, textViewAuthor)
            bindingStrategy.bindThumbnail(it, imageVieThumbnail)
            bindingStrategy.bindCreationTime(it, textViewCreationTime)
        }
    }

}