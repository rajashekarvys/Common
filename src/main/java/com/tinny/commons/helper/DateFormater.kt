package com.tinny.commons.helper

fun msToMinSec(durationInMillis:Long):String{
    val millis = (durationInMillis % 1000)/10
    val second = durationInMillis / 1000 % 60
    val minute = durationInMillis / (1000 * 60) % 60
    return String.format("%02d:%02d:%02d", minute, second, millis)
}