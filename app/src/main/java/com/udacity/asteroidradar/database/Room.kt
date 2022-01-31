package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

private lateinit var INSTANCE: AsteroidsDatabase

@Dao
interface AsteroidDao {

    @Query("select * from DatabaseAsteroid ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query(
        "select * from DatabaseAsteroid WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC"
    )
    fun getAsteroidsWithinTimeSpan(startDate: Date, endDate: Date): LiveData<List<DatabaseAsteroid>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)

    @Query("DELETE FROM DatabaseAsteroid WHERE closeApproachDate < :lastDate")
    fun deleteAllBefore(lastDate: Date): Int

}

@Dao
interface PictureOfDayDao {

    @Query("select * from DatabasePictureOfDay ORDER BY ID DESC")
    fun getAllDailyPictures(): LiveData<List<DatabasePictureOfDay>>


    @Query("select * from DatabasePictureOfDay WHERE mediaType == 'image' AND date <= :currentDate ORDER BY date DESC LIMIT 1")
    fun getLastDailyPictureWithImage(currentDate: Date): LiveData<DatabasePictureOfDay>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(picture: DatabasePictureOfDay)
}

@Database(entities =[DatabaseAsteroid::class,DatabasePictureOfDay::class], version = 1)
@TypeConverters(MyConverters::class)
abstract class AsteroidsDatabase:  RoomDatabase(){
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfDayDao: PictureOfDayDao
}

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}