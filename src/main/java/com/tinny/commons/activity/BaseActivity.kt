package com.tinny.commons.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tinny.commons.R
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

    }
    abstract fun getAppIconID(): ArrayList<Int>

    abstract fun getAppLauncherName(): String



}
