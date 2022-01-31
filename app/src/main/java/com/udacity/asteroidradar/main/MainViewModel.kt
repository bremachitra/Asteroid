package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidMenuFilter
import com.udacity.asteroidradar.AsteroidUtils
import com.udacity.asteroidradar.AsteroidUtils.Companion.getDateAndTimeBeforeOrAfterNow
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MainViewModel(application: Application) : ViewModel() {

    private val _navigateToAsteroid = MutableLiveData<Asteroid>()

    val navigateToAsteroid: LiveData<Asteroid>
        get() = _navigateToAsteroid

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    var asteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao.getAsteroidsWithinTimeSpan(
                AsteroidUtils.getDateWithoutTime(),
                AsteroidUtils.addOneDate(AsteroidUtils.getDateWithoutTime())
            )
        ) {
            it.asDomainModel()
        }

    var dailyPictureData: LiveData<PictureOfDay?> = getDailyPicture()

    private fun getDailyPicture(): LiveData<PictureOfDay?> {
        val map = Transformations.map(
            database.pictureOfDayDao.getLastDailyPictureWithImage(AsteroidUtils.getDateWithoutTime())
        ) {
            it?.asDomainModel()
        }
        return map
    }

    init {
        Timber.i("Init block")
        refreshAsteroids()
        refreshPictureOfDay()
        deleteOldUnusedAsteroids()
    }

    private fun refreshAsteroids() {
        viewModelScope.launch {
            try {
                Timber.i("Refresh asteroids")
                asteroidRepository.refreshAsteroids()

            } catch (e: Exception) {
                Timber.i(" exception: ${e.message}")
                Timber.i(" exception : ${e.stackTrace}")
            }
        }

    }
    private fun refreshPictureOfDay() {
        viewModelScope.launch {
            try {

                asteroidRepository.refreshPictureOfDay()

            } catch (e: Exception) {
                Timber.i(" exception ${e.message}")
                Timber.i("exception ${e.stackTrace}")
            }
        }
    }

    private fun deleteOldUnusedAsteroids() {
        viewModelScope.launch {
            try {
                val timeOneWeekBefore = getDateAndTimeBeforeOrAfterNow(-7)
                asteroidRepository.deleteAsteroids(timeOneWeekBefore)

            } catch (e: Exception) {
                Timber.i("exception ${e.message}")
                Timber.i("exception ${e.stackTrace}")
            }
        }
    }

    fun navigateToAsteroidDetailsFinished() {
        _navigateToAsteroid.value = null
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    class ViewModelFactory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Exception viewmodel factory")
        }
    }



    fun filterAsteroids(filter: AsteroidMenuFilter) {
        Timber.i("filterAsteroids(): filter: $filter")
        when (filter) {
            AsteroidMenuFilter.TODAT_ASTEROIDS -> {
                val date = AsteroidUtils.getDateWithoutTime()
                filterAsteroids(date, date)
            }
            AsteroidMenuFilter.WEEK_ASTEROIDS -> {
                val startDate = AsteroidUtils.getDateWithoutTime()
                filterAsteroids(
                    startDate,
                    AsteroidUtils.addSixDates(startDate)
                )
            }
            else -> {
                filterAsteroids()
            }
        }
    }


    private fun filterAsteroids(startDate: Date? = null, endDate: Date? = null) {
        asteroids = if (startDate != null && endDate != null) {
                  Transformations.map(
                database.asteroidDao.getAsteroidsWithinTimeSpan(
                    startDate,
                    endDate
                )
            ) {
                it.asDomainModel()
            }
        } else {
            val databaseAsteroidsLiveData: LiveData<List<DatabaseAsteroid>> =
                database.asteroidDao.getAllAsteroids()
            Transformations.map(databaseAsteroidsLiveData) {
                it.asDomainModel()
            }
        }

    }
}