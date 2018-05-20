package com.arctouch.codechallenge.extensions

import android.widget.ImageView
import com.arctouch.codechallenge.MainApplication
import com.arctouch.codechallenge.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadUrl(url: String){

    Glide.with(MainApplication.getInstance().applicationContext)
            .load(url)
            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
            .into(this)

}