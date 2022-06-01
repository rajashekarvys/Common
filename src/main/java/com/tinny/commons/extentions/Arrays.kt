package com.tinny.commons.extentions

import java.util.*


fun IntArray.shuffle(): IntArray {
    val rng = Random()

    for (index in this.indices) {
        val randomIndex = rng.nextInt(index + 1)

        // Swap with the random position
        val temp = this[index]
        this[index] = this[randomIndex]
        this[randomIndex] = temp
    }

    return this
}

fun <T> Array<T>.shuffle(): Array<T> {
    val rng = Random()

    for (index in 1 until this.size) {
        val randomIndex = rng.nextInt(index)

        // Swap with the random position
        val temp = this[index]
        this[index] = this[randomIndex]
        this[randomIndex] = temp
    }

    return this
}

