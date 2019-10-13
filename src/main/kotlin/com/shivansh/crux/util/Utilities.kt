package com.shivansh.crux.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale

fun Any?.isNull() = (this == null)

val HTML5_DATE_FORMATTER = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")

fun formatTimeDuration(milliSec: Long): String {
    var milliSec = milliSec
    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60

    val elapsedHours = milliSec / hoursInMilli
    milliSec %= hoursInMilli
    val elapsedMinutes = milliSec / minutesInMilli
    milliSec %= minutesInMilli
    val elapsedSeconds = milliSec / secondsInMilli
    milliSec %= secondsInMilli

    if (elapsedHours > 0)
        return String.format(Locale.ENGLISH, "%d Hours", elapsedHours)
    if (elapsedMinutes > 0)
        return String.format(Locale.ENGLISH, "%d Mins", elapsedMinutes)
    return if (elapsedSeconds > 0) String.format(Locale.ENGLISH, "%d Sec", elapsedSeconds) else "0 Sec"
}

fun formatElapsedTime(eventTime: Date, currentTime: Date): String {
    var diffTime = currentTime.time - eventTime.time
    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24
    val elapsedDays = diffTime / daysInMilli
    diffTime %= daysInMilli
    val elapsedHours = diffTime / hoursInMilli
    diffTime %= hoursInMilli
    val elapsedMinutes = diffTime / minutesInMilli
    diffTime %= minutesInMilli
    if (elapsedDays > 0)
        return String.format(Locale.ENGLISH, "%d days ago", elapsedDays)
    if (elapsedHours > 0)
        return String.format(Locale.ENGLISH, "%d hours ago", elapsedHours)
    return if (elapsedMinutes > 0) String.format(Locale.ENGLISH, "%d minutes ago", elapsedMinutes) else "Now"
}

fun formatElapsedTime(eventTime: Date): String = formatElapsedTime(eventTime, Calendar.getInstance().time)

fun formatCompleteDate(date: Date): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
    return formatter.format(date)
}

fun getCurrentFormattedDate(): String {
    val date = Calendar.getInstance().time
    return getFormattedDate(date)
}

fun getFormattedDate(date: Date): String {
    val simpleDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return simpleDateFormat.format(date)
}