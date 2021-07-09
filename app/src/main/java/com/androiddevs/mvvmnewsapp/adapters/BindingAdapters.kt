package com.androiddevs.mvvmnewsapp.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.androiddevs.mvvmnewsapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("imageUrl")
fun bindImage(imgView : ImageView , imgUrl : String?){
    imgUrl.let {
        Glide.with(imgView.context)
            .load(imgUrl)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}