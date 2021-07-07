package com.androiddevs.mvvmnewsapp.adapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.viewmodel.ApiStatus
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

@BindingAdapter("apiStatus")
fun bindStatus(imgView: ImageView , status : ApiStatus?){

    if (status != null) when(status){
            ApiStatus.DONE -> {
                imgView.visibility = View.GONE
            }
            ApiStatus.LOADING -> {
                imgView.visibility = View.GONE
            }
            ApiStatus.ERROR -> {
                imgView.setImageResource(R.drawable.ic_connection_error)
                imgView.visibility = View.VISIBLE
            }
            null -> imgView.visibility = View.GONE
        }

//    Log.i("Exception","apiStatus")
//    status?.let {
//        if(it.value==true){
//            Log.i("Exception","apiStatus2")
//            imgView.visibility = View.VISIBLE
//            imgView.setImageResource(R.drawable.ic_connection_error)
//        }
//        else{
//            imgView.visibility = View.GONE
//        }
//    }
}