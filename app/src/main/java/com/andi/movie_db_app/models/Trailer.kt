package com.andi.movie_db_app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Trailer(
    @field:SerializedName("key")
    val key:String? = null
): Parcelable
