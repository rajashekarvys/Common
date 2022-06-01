package com.tinny.commons.helper

fun msToMinSec(durationInMillis: Long): String {
    val millis = (durationInMillis % 1000) / 10
    val second = durationInMillis / 1000 % 60
    val minute = durationInMillis / (1000 * 60) % 60
    return if (minute == 0L) {
        String.format("%02d:%02d", second, millis)
    } else {
        String.format("%02d:%02d:%02d", minute, second, millis)
    }
}

fun msToMinSecs(durationInMillis: Long): String {
    val second = durationInMillis / 1000 % 60
    val minute = durationInMillis / (1000 * 60) % 60
    return String.format("%02d:%02d", minute, second)
}

fun msToMinSecsAddText(durationInMillis: Long): String {
    val second = durationInMillis / 1000 % 60
    val minute = durationInMillis / (1000 * 60) % 60
    var finalData =  String.format("%02d:%02d", minute, second)
    val data = finalData.split(":")
    finalData = data[0] + "m:"+data[1]+"s"
    return finalData
}
fun msToMillSecsAddText(durationInMillis: Long): String {
    val millis = (durationInMillis % 1000) / 10
    val second = durationInMillis / 1000 % 60
    val minute = durationInMillis / (1000 * 60) % 60

    return if (minute == 0L) {
        val data =  String.format("%02d:%02d", second, millis).split(":")
         data[0] + "s: "+data[1]+"ms"
    } else {
        val data =  String.format("%02d:%02d:%02d", minute, second, millis)
        data[0] + "m: "+data[1]+"s " + data[2]+"ms"
    }
}
fun minSecToMs(time: String): String {

/*
    val millis = (durationInMillis % 1000)/10
    val second = durationInMillis / 1000 % 60
    val minute = durationInMillis / (1000 * 60) % 60
*/

    val timeArray = time.split(":")


    val mill = (timeArray[2].toInt() * 10000)
    val second = (timeArray[1].toInt() * 10000)
    val min = (timeArray[2].toInt() * 10000)
    var timeInMill = mill + second + min


    return timeInMill.toString()
}

fun secToMinSec(durationInSec: Int): String {
    val second = durationInSec % 60
    val minute = durationInSec / (60) % 60
    val hours = (durationInSec / (60 * 60) % 24)
    return if (hours != 0) {
        String.format("%02d:%02d:%02d", hours, minute, second)
    } else {
        String.format("%02d:%02d", minute, second)
    }
}