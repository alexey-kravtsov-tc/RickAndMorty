package com.reddit.rickmorty.model.dto

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val gender: String,
    val image: String
) {
    companion object {
        @JvmStatic
        @BindingAdapter("imageSource")
        fun ImageView.setImageSource(url: String?) {
            url.let { Picasso.get().load(it).into(this) }
        }
    }
}