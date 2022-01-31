package com.udacity.asteroidradar

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.AsteroidUtils.Companion.toYearMonthsDays
import com.udacity.asteroidradar.main.AsteroidAdapter
import java.util.*

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}
@BindingAdapter("dateWithoutTime")
fun bindTextViewToDate(textView: TextView, date: Date) {
    textView.text = date.toYearMonthsDays()
}


@BindingAdapter("closeApproachDateDescription")
fun bindDescriptionToCloseApproachDateDescription(linearLayout: LinearLayout, asteroid: Asteroid) {
    val context = linearLayout.context
    linearLayout.contentDescription =
        "${context.getString(R.string.close_approach_data_title)} is ${asteroid.closeApproachDate.toYearMonthsDays()}"
}


@BindingAdapter("asteroidList")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    asteroids: List<Asteroid>?
) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    adapter.submitList(asteroids) {
        recyclerView.scrollToPosition(0)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
