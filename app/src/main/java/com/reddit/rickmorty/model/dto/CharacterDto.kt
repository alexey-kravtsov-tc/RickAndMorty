package com.reddit.rickmorty.model.dto

import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.reddit.rickmorty.R
import com.squareup.picasso.Picasso

class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val gender: String,
    val image: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(status)
        parcel.writeString(gender)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CharacterDto> {
        override fun createFromParcel(parcel: Parcel): CharacterDto {
            return CharacterDto(parcel)
        }

        override fun newArray(size: Int): Array<CharacterDto?> {
            return arrayOfNulls(size)
        }

        @JvmStatic
        @BindingAdapter("imageSource")
        fun ImageView.setImageSource(url: String?) {
            url.let { Picasso.get().load(it).placeholder(R.drawable.ic_person).into(this) }
        }
    }
}