package com.udacity.asteroidradar.api


import android.net.ParseException
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidUtils
import com.udacity.asteroidradar.PictureOfDay
import timber.log.Timber
import java.util.*

fun parseAsteroids(asteroidsFullData: Map<*, *>): List<Asteroid> {
    val nearEarthObjects: Map<*, *>
    try {
        nearEarthObjects = getNearEarthObjects(asteroidsFullData)
        if (nearEarthObjects.isEmpty()) return emptyList()
    } catch (exc: ParseException) {
        Timber.e("Parse error  $exc.message")
        return emptyList()
    }

    val domainAsteroids = mutableListOf<Asteroid>()

    for (asteroidsOfDayPair: Pair<*, *> in nearEarthObjects.toList()) {
        val arrAsteroids: ArrayList<*> = asteroidsOfDayPair.second as ArrayList<*>

        for (asteroid in arrAsteroids) {
            if (asteroid is Map<*, *>) {
                val asteroidParseResult =
                    try {
                        parseEachAsteroid(asteroid)
                    } catch (exc: ParseException) {
                        Timber.e("Parse error single asteroid $exc.message")
                    }

                if (asteroidParseResult is Asteroid) {
                    domainAsteroids += asteroidParseResult
                    Timber.i("Parsed asteroid: ${asteroidParseResult.codename} : ${asteroidParseResult.closeApproachDate}")
                }
            }
        }

    }

    Timber.i(" count of asteroids = ${domainAsteroids.count()}, last date: ${domainAsteroids.last().closeApproachDate}")
    return domainAsteroids
}

 private fun parseEachAsteroid(asteroid: Map<*, *>): Asteroid {
    val id: Long = (asteroid["id"] as String)
        .toLong()
    val codename = asteroid["name"] as String

    val closeApproachData = (asteroid["close_approach_data"]
            as java.util.ArrayList<*>)[0] as Map<*, *>

    val closeApproachDate = (closeApproachData["close_approach_date"]) as String

    val absoluteMagnitude = asteroid["absolute_magnitude_h"]
            as Double

    val estimatedDiameterKm = ((asteroid["estimated_diameter"]
            as Map<*, *>)["kilometers"]
            as Map<*, *>)["estimated_diameter_max"]
            as Double

    val relativeVelocity = ((((closeApproachData["relative_velocity"])
            as Map<*, *>)["kilometers_per_second"])
            as String)
        .toDouble()

    val distanceFromEarth = ((((closeApproachData["miss_distance"])
            as Map<*, *>)["astronomical"])
            as String)
        .toDouble()

    val isPotentiallyHazardous = asteroid["is_potentially_hazardous_asteroid"]
            as Boolean

    val date: Date = AsteroidUtils.getDateFromString(closeApproachDate, id)

    return Asteroid(
        id = id,
        codename = codename,
        closeApproachDate = date,
        absoluteMagnitude = absoluteMagnitude,
        estimatedDiameter = estimatedDiameterKm,
        relativeVelocity = relativeVelocity,
        distanceFromEarth = distanceFromEarth,
        isPotentiallyHazardous = isPotentiallyHazardous
    )
}

fun parsePictureOfDay(rawPictureData: Map<*, *>): PictureOfDay {
    val rawDate = rawPictureData["date"] as String
    val date = AsteroidUtils.getDateFromString(
        rawDate,
        "Parsing PictureOfDay failed"
    )

    return PictureOfDay(
        id = rawDate,
        date = date,
        title = rawPictureData["title"] as String,
        mediaType = rawPictureData["media_type"] as String,
        url = rawPictureData["url"] as String
    )
}
private fun getNearEarthObjects(asteroidsFullData: Map<*, *>) =
    asteroidsFullData["near_earth_objects"] as Map<*, *>