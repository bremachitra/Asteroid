package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import java.util.*

@Entity
data class DatabaseAsteroid constructor(
@PrimaryKey
val id: Long,
val codename: String,
val closeApproachDate: Date,
val absoluteMagnitude: Double,
val estimatedDiameter: Double,
val relativeVelocity: Double,
val distanceFromEarth: Double,
val isPotentiallyHazardous: Boolean

    )
@Entity
data class DatabasePictureOfDay constructor (
    @PrimaryKey
    val id: String,
    val date: Date,
    val mediaType: String,
    val title: String,
    val url: String
        )


fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}
    fun DatabasePictureOfDay.asDomainModel(): PictureOfDay {
        return PictureOfDay(
            id = this.id,
            date = this.date,
            mediaType = this.mediaType,
            title = this.title,
            url = this.url

        )
    }
