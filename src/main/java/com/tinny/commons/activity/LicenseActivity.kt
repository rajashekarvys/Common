package com.tinny.commons.activity

import android.os.Bundle
import com.tinny.commons.R
import java.util.*

class LicenseActivity : BaseActivity() {
    override fun getAppIconID(): ArrayList<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAppLauncherName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license)
    }
}
