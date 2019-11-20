package com.tinny.commons.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tinny.commons.Intent_AppIcon
import com.tinny.commons.Intent_AppName
import com.tinny.commons.Intent_AppVesion
import com.tinny.commons.R
import com.tinny.commons.extentions.getStoreUrl
import com.tinny.commons.extentions.launchViewIntent
import com.tinny.commons.extentions.setFont
import com.tinny.commons.extentions.setFontWithColor
import kotlinx.android.synthetic.main.about_row.view.*
import kotlinx.android.synthetic.main.activity_about.*
import java.util.*

class AboutActivity : AppCompatActivity(),View.OnClickListener {


//    I am new 2.0 Yes 
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.moreApps ->{
                launchViewIntent("https://play.google.com/store/apps/developer?id=Tinnymobileapps")
            }
            R.id.rateUs ->{launchViewIntent("market://details?id=$packageName")}
            R.id.reportBugs ->{reportBug()}
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
        setupCopyright()

        moreApps.txtIcon.setFontWithColor(this, getString(R.string.f_more_apps), "icomoon_common.ttf", resources.getColor(R.color.md_grey_black))
        rateUs.txtIcon.setFontWithColor(this, getString(R.string.f_full_star), "icomoon_common.ttf", resources.getColor(R.color.md_grey_black))
        share.txtIcon.setFontWithColor(this, getString(R.string.f_fav), "icomoon_common.ttf", resources.getColor(R.color.md_grey_black))
        reportBugs.txtIcon.setFontWithColor(this, getString(R.string.f_bug), "icomoon_common.ttf", resources.getColor(R.color.md_grey_black))

        Toast.makeText(this,packageName,Toast.LENGTH_SHORT).show()
        moreApps.setOnClickListener(this)
        rateUs.setOnClickListener(this)
        share.setOnClickListener(this)
        reportBugs.setOnClickListener(this)
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
        val versionName = intent.getStringExtra(Intent_AppVesion)
        val year = Calendar.getInstance().get(Calendar.YEAR)
        txtCopyrights.text = String.format(getString(R.string.copyright), versionName, year)
    }

    private fun reportBug(){
        val mIntent = Intent(Intent.ACTION_SENDTO)
        mIntent.data = Uri.parse("mailto:")
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("tinnymobileapps@gmail.com"))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, intent.getStringExtra(Intent_AppName) + " (v-"+ intent.getStringExtra(Intent_AppVesion) + " -- "+ getString(R.string.report_bug_desc))
        startActivity(Intent.createChooser(mIntent, "Send Email"))
    }

}
