package com.tinny.commons.extentions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.tinny.commons.*
import com.tinny.commons.activity.AboutActivity
import com.tinny.commons.helper.AppLogger
import java.io.File

fun Activity.setAsIntent(file: File, applicationId: String) {
    Thread {
        val newUri = getFilePublicUri(file, applicationId)
        Intent().apply {
            action = Intent.ACTION_ATTACH_DATA
            setDataAndType(newUri, getUriMimeType(file.absolutePath, newUri))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val chooser = Intent.createChooser(this, getString(R.string.set_as))

            if (resolveActivity(packageManager) != null) {
                startActivityForResult(chooser, REQUEST_SET_AS)
            } else {
                toast(R.string.app_not_found)
            }
        }
    }.start()
}

fun Activity.openIntent(file: File, applicationId: String) {
    Thread {
        val newUri = getFilePublicUri(file, applicationId)
        val mimeType = getUriMimeType(file.absolutePath, newUri)
        Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(newUri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra("real_file_path_2", file.absolutePath)

            if (resolveActivity(packageManager) != null) {
                val chooser = Intent.createChooser(this, getString(R.string.open_with))
                try {
                    startActivity(chooser)
                } catch (e: NullPointerException) {
                    e.message
                }
            } else {
                toast(R.string.app_not_found)

            }
        }
    }.start()
}

fun Activity.shareMultipleImagesIntent(filesToSend: ArrayList<String>, applicationId: String) {
    Thread {

        val intent = Intent()
        intent.action = Intent.ACTION_SEND_MULTIPLE
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.")
        intent.type = "image/*"
        val files = ArrayList<Uri>()

        for (path in filesToSend /* List of the files you want to send */) {
            val newUri = getFilePublicUri(File(path), applicationId) ?: return@Thread
            files.add(newUri)
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files)
        startActivity(intent)
    }.start()
}
fun Activity.shareIntent(path: String, applicationId: String) {
    try{
        AppLogger.debugLogs("Test shareIntent==","path == $path applicationId == $applicationId")
        Thread {
            val newUri = getFilePublicUri(File(path), applicationId) ?: return@Thread
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, newUri)
                type = getUriMimeType(path, newUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                try {
                    if (resolveActivity(packageManager) != null) {
                        startActivity(Intent.createChooser(this, getString(R.string.share_via)))
                    } else {
                        toast(R.string.app_not_found)
                    }
                } catch (e: RuntimeException) {

                }
            }
        }.start()
    }catch (e: Exception){
        toast(R.string.not_able_to_share)
    }

}

fun Activity.lauchAboutUs(icon:Int, appName:String, packageName:String, appVersion:String, showTranslation:Boolean = true){
    val intent = Intent(this,AboutActivity::class.java).apply {
        putExtra(Intent_AppIcon,icon)
        putExtra(Intent_AppPACKAGENAME,packageName)
        putExtra(Intent_AppName,appName)
        putExtra(Intent_Translation,showTranslation)
        putExtra(Intent_AppVesion,appVersion)

    }
    startActivity(intent)
}

fun Activity.launchViewIntent(id: Int) = launchViewIntent(getString(id))

fun Activity.launchViewIntent(url: String) {
    Thread {
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            if (resolveActivity(packageManager) != null) {
                startActivity(this)
            } else {
                toast(R.string.app_not_found)
            }
        }
    }.start()

}
