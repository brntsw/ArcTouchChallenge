package com.arctouch.codechallenge.extensions

import android.annotation.SuppressLint
import android.widget.ImageView
import com.arctouch.codechallenge.MainApplication
import com.arctouch.codechallenge.R
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions

@SuppressLint("CheckResult")
@JvmOverloads
fun ImageView.loadUrl(url: String, hasPlaceholder: Boolean? = false){

    val builder = Glide.with(MainApplication.getInstance().applicationContext)
            .load(url)

    if(hasPlaceholder != null && hasPlaceholder){
        builder.apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
    }

    builder.into(this)

}