package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidUtils
import com.udacity.asteroidradar.AsteroidUtils.Companion.toAsteroidString
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroids

import com.udacity.asteroidradar.api.parsePictureOfDay
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.network.AsteroidApiService
import com.udacity.asteroidradar.network.asDatabaseModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*


class AsteroidRepository (val database : AsteroidsDatabase){

    suspend fun refreshAsteroids() {
        val currentDate = AsteroidUtils.dateWithoutTime()
        val startDate: String = currentDate.toAsteroidString()
        val endDate = AsteroidUtils.addSixDates(currentDate).toAsteroidString()

        Timber.i("refreshAsteroids startDate: $startDate, endDate: $endDate")

        withContext(Dispatchers.IO) {
            val asteroidsFullData = AsteroidApiService.AsteroidsApi.retrofitService.getAsteroids(startDate, endDate)
        as Map<*,*>
            val asteroids: List<Asteroid> = parseAsteroids(asteroidsFullData)

            database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
        }

    }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val rawDailyPicture = AsteroidApiService.AsteroidsApi.retrofitService.getPictureOfDay() as Map<*, *>
                val dailyPicture: PictureOfDay = parsePictureOfDay(rawDailyPicture)
                Timber.i(": $rawDailyPicture")
                database.pictureOfDayDao.insert(dailyPicture.asDatabaseModel())
            } catch (exc: Exception) {
                Timber.e("Exception  ${exc.message}")
            }
        }


    }

        suspend fun deleteAsteroids(endDate: Date) {
            withContext(Dispatchers.IO) {
                val deletedElementsCount: Int = database.asteroidDao.deleteAllBefore(endDate)
                    }
        }


}

