package com.udacity.asteroidradar.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidUtils.Companion.toYearMonthsDays
import com.udacity.asteroidradar.R
import timber.log.Timber
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding


class AsteroidAdapter(val onClickListener: OnClickListener, val context: Context?) :
    ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DiffCallback) {
    companion object DiffCallback :
        DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    class AsteroidViewHolder(private var binding: AsteroidListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: OnClickListener, asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.clickListener = listener

            binding.executePendingBindings()
        }

        companion object {

            fun from(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidListItemBinding.inflate(layoutInflater, parent, false)
                return AsteroidViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):
            AsteroidViewHolder {

        return AsteroidViewHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: AsteroidViewHolder,
        position: Int
    ) {
        Timber.i("onBindViewHolder")
        val asteroid = getItem(position)
        holder.bind(onClickListener, asteroid)
        holder.itemView.contentDescription =
            "${getTextualHasardousnessRating(asteroid.isPotentiallyHazardous)} " +
                    "Asteroid with codename: ${asteroid.codename}. " +
                    "Close approach date : ${asteroid.closeApproachDate.toYearMonthsDays()}."
    }

    private fun getTextualHasardousnessRating(isPotentiallyHazardous: Boolean) =
        if (isPotentiallyHazardous) {
            context?.getString(R.string.HazardousText) ?: "hazardous"
        } else {
            context?.getString(R.string.SafeText) ?: "safe"
        }

    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}


