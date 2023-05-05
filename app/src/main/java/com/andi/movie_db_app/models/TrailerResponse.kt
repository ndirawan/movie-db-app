package com.andi.movie_db_app.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrailerResponse (
    @field:SerializedName("results")
    val results:MutableList<Trailer>

    ):Parcelable