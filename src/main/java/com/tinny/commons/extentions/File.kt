package com.tinny.commons.extentions

import java.io.File


fun File.getSize(): Long {
    return if (isDirectory) {
        getDirectorySize(this)
    } else {
        length()
    }
}


fun File.getFilesCount(): Int {
    return if (isDirectory) {
        listFiles().size
    } else {
        1
    }
}

fun File.getThmabnilPath(): String {
    return if (isDirectory) {
        listFiles()[0].absolutePath
    } else {
        absolutePath
    }
}

fun File.sizeInString():String{
    return fileSize(getSize())
}

private fun getDirectorySize(dir: File): Long {
    var size = 0L
    if (dir.exists()) {
        val files = dir.listFiles()
        if (files != null) {
            for (i in files.indices) {
                if (files[i].isDirectory) {
                    size += getDirectorySize(files[i])
                } else {
                    size += files[i].length()
                }
            }
        }
    }
    return size
}

/**
 * @return Array<Long> fileSize and Count
 *
 * */
fun File.getDirectorySize(type: Array<String>): Array<Long> {
    var size = 0L
    var count = 0L
    if (exists()) {
        val files = listFiles()
        if (files != null) {
            for (i in files.indices) {
                if (files[i].isDirectory) {
                    size += getDirectorySize(files[i])
                } else {
                    if (type.contains(files[i].extension)) {
                        size += files[i].length()
                        count++
                    }
                }
            }
        }
    }
    return arrayOf(size,count)
}

private fun  fileSize(size: Long): String {

    var fileSize = size / (1024f)
    var fileSizeString = String.format("%.2f", fileSize) + "KB"

    if (fileSize > 1024f) {
        fileSize /= 1024f
        fileSizeString = String.format("%.2f", fileSize) + "MB"

    }
    if (fileSize > 1024f) {
        fileSize /= 1024f
        fileSizeString = String.format("%.2f", fileSize) + "GB"
    }

    return fileSizeString
}



