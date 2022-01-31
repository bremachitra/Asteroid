package com.udacity.asteroidradar


import java.text.SimpleDateFormat
import java.util.*


enum class AsteroidMenuFilter(val value:String){
    WEEK_ASTEROIDS ("week"),
    TODAT_ASTEROIDS("today"),
    SAVED_ASTEROIDS("saved")
}
class AsteroidUtils {


    companion object{

        fun dateWithoutTime(): Date {
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.time
        }
        fun addSixDates(date: Date) = addDays(date, 6)

        fun addOneDate(date: Date) = addDays(date, 1)

        private fun addDays(date: Date, days: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, days)
            return calendar.time
        }
        fun Date.toYearMonthsDays(): String = this.toString(Constants.API_QUERY_DATE_FORMAT)

        fun getDateFromString(
            formattedDate: String,
            exceptionMessagePart: String = ""
        ): Date {
            val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.US)
            return formatter.parse(formattedDate)
                ?: throw Exception(
                    "formatted date $formattedDate leads to null. $exceptionMessagePart"
                )
        }

        fun getDateFromString(date: String, id: Long): Date {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return formatter.parse(date)
                ?: throw Exception(
                    "closeApproachDate $date for asteroid with id $id leads to null"
                )
        }
        fun getDateWithoutTime(): Date {
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.time
        }

        fun getDateAndTimeBeforeOrAfterNow(offset: Int) =
            Date(System.currentTimeMillis() + offset * 24 * 60 * 60 * 1000)

        // COnvert date to yyyy-MM-dd format
        fun Date.toAsteroidString(): String = this.toString(Constants.API_QUERY_DATE_FORMAT)

        //
        private fun Date.toString(
            format: String,
            locale: Locale = Locale.getDefault()
        ) = SimpleDateFormat(format, locale).format(this)

    }

}