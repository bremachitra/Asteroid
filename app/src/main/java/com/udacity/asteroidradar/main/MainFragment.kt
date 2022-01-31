package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.AsteroidMenuFilter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
        }
        ViewModelProvider(
            this,
            MainViewModel.ViewModelFactory(activity.application)
        ).get(MainViewModel::class.java)
    }

    private lateinit var asteroidAdapter: AsteroidAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = AsteroidAdapter(
            AsteroidAdapter.OnClickListener { asteroid ->
                viewModel.displayAsteroidDetails(asteroid)
            }, context?.applicationContext
        )
            .apply {
                asteroidAdapter = this
            }

        viewModel.navigateToAsteroid.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.navigateToAsteroidDetailsFinished()
            }
        })


        viewModel.dailyPictureData.observe(viewLifecycleOwner, Observer {
            it?.let { dailyPicture ->
                Picasso.get().load(dailyPicture.url).into(binding.activityMainImageOfTheDay)
                binding.activityMainImageOfTheDay.contentDescription = dailyPicture.title
                binding.titleOfPictureOfDayTextView.text = dailyPicture.title
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        updateReferenceOfAsteroidAdapter()
        viewModel.filterAsteroids(
        when (item.itemId)
        {
            R.id.show_week_asteroids -> AsteroidMenuFilter.WEEK_ASTEROIDS
            R.id.show_today_asteroids -> AsteroidMenuFilter.TODAT_ASTEROIDS
            else -> AsteroidMenuFilter.SAVED_ASTEROIDS
        })
        return true
    }

    private fun updateReferenceOfAsteroidAdapter() {
        viewModel.asteroids.observe(viewLifecycleOwner) {
            asteroidAdapter.submitList(it)

        }
    }
}
