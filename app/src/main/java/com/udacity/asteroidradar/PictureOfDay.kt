package com.udacity.asteroidradar

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PictureOfDay(val id:String,
                        val date: Date,
                        @Json(name = "media_type") val mediaType: String,
                        val title: String,
                        val url: String):Parcelable