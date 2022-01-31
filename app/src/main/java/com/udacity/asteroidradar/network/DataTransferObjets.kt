package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.DatabasePictureOfDay

    fun List<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
        return this.map {
            DatabaseAsteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous)
        }.toTypedArray()
    }


    fun PictureOfDay.asDatabaseModel(): DatabasePictureOfDay {
        return DatabasePictureOfDay(
            id = this.id,
            date =this.date,
            mediaType = this.mediaType,
            title = this.title,
            url = this.url
        )
    }
