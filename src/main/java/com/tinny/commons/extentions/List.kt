package com.tinny.commons.extentions

fun <T> List<T>.toStringComma(): String {

    var data = ""
    for (item in this) {
        data = "$data$item,"
    }
    data  = data.removeSuffix(",")
    return data
}