package com.tinny.commons.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tinny.commons.R
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.row.view.*
import java.util.*

class AboutActivity : AppCompatActivity(),View.OnClickListener {

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.moreApps ->{ launchViewIntent("https://play.google.com/store/apps/dev?id=9070296388022589266") }
            R.id.rateUs ->{launchViewIntent("market://details?id=$packageName")}
            R.id.reportBugs ->{}
            R.id.share ->{onClickShare()}
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        txtAppName.text = getString(R.string.app_name)
        imgApp.setImageResource(intent.getIntExtra(Intent_AppIcon,0))

        moreApps.txtTitle.text = getString(R.string.more_apps)
        moreApps.txtDesc.text = getString(R.string.more_apps_desc)

        rateUs.txtTitle.text = getString(R.string.rate_us)
        rateUs.txtDesc.text =getString(R.string.rate_us_desc)

        share.txtTitle.text = getString(R.string.share)
        share.txtDesc.text =getString(R.string.share_desc)

        reportBugs.txtTitle.text = getString(R.string.report_bug)
        reportBugs.txtDesc.text =getString(R.string.report_bug_desc)

        moreApps.txtIcon.setFont(this,getString(R.string.f_more_apps),"icomoon_common.ttf")
        rateUs.txtIcon.setFont(this,getString(R.string.f_full_star),"icomoon_common.ttf")
        share.txtIcon.setFont(this,getString(R.string.f_fav),"icomoon_common.ttf")
        reportBugs.txtIcon.setFont(this,getString(R.string.f_bug),"icomoon_common.ttf")

        Toast.makeText(this,packageName,Toast.LENGTH_SHORT).show()
        moreApps.setOnClickListener(this)
        rateUs.setOnClickListener(this)
        share.setOnClickListener(this)
        reportBugs.setOnClickListener(this)
        setupCopyright()
    }



    private fun onClickShare() {

            val text = String.format(getString(R.string.share_text), intent.getStringExtra(Intent_AppName), getStoreUrl())
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, intent.getStringExtra(Intent_AppName))
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
                startActivity(Intent.createChooser(this, getString(R.string.invite_via)))
            }

    }

    private fun setupCopyright() {
        val versionName = intent.getStringExtra(intent.getStringExtra(Intent_AppVesion)) ?: ""
        val year = Calendar.getInstance().get(Calendar.YEAR)
        txtCopyrights.text = String.format(getString(R.string.copyright), versionName, year)
    }


}
