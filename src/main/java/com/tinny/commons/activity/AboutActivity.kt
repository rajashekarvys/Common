package com.tinny.commons.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tinny.commons.*
import com.tinny.commons.extentions.*
import com.tinny.commons.helper.CommonConstants
import com.tinny.commons.helper.CommonTextIcons
import kotlinx.android.synthetic.main.about_row.view.*
import kotlinx.android.synthetic.main.activity_about.*
import java.util.*

class AboutActivity : AppCompatActivity(),View.OnClickListener {

    val emailId= "mobilepuzzlesgames@gmail.com"
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.moreApps ->{
//                launchViewIntent("https://play.google.com/store/apps/developer?id=Tinnymobileapps")
                launchViewIntent("https://play.google.com/store/apps/developer?id=Simple+Puzzle+games")

            }
            R.id.rateUs ->{launchViewIntent("market://details?id=$packageName")}
            R.id.reportBugs ->{reportBug()}
            R.id.share ->{onClickShare()}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        intent.getStringExtra(Intent_AppName)
        txtAppName.text = intent.getStringExtra(Intent_AppName)
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

        if (intent.getBooleanExtra(Intent_Translation,true)){
            rrTrans.makeVisible()
        }else{
            rrTrans.makeGone()
        }

        moreApps.txtIcon.setFontWithColor(this, getString(R.string.f_more_apps), CommonConstants.icomoonCommon, resources.getColor(R.color.md_grey_black))
        rateUs.txtIcon.setFontWithColor(this, getString(R.string.f_full_star), CommonConstants.icomoonCommon, resources.getColor(R.color.md_grey_black))
        share.txtIcon.setFontWithColor(this, getString(R.string.f_fav), CommonConstants.icomoonCommon, resources.getColor(R.color.md_grey_black))
        reportBugs.txtIcon.setFontWithColor(this, getString(R.string.f_bug), CommonConstants.icomoonCommon, resources.getColor(R.color.md_grey_black))
        txtIconTran.setFontWithColor(this, CommonTextIcons.F_Translation, CommonConstants.icomoonCommon, resources.getColor(R.color.md_grey_black))



        txtTranHelp.setOnClickListener {
            val mIntent = Intent(Intent.ACTION_SENDTO)
            mIntent.data = Uri.parse("mailto:")
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailId))
            mIntent.putExtra(Intent.EXTRA_SUBJECT, intent.getStringExtra(Intent_AppName) + " (v-"+ intent.getStringExtra(Intent_AppVesion) + " -- "+ "Helping in text translation")
            startActivity(Intent.createChooser(mIntent, "Send Email"))
        }
//        Toast.makeText(this,packageName,Toast.LENGTH_SHORT).show()
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
        txtCopyrights.text = String.format(getString(R.string.copyright1), versionName, year)
    }

    private fun reportBug(){
        val mIntent = Intent(Intent.ACTION_SENDTO)
        mIntent.data = Uri.parse("mailto:")
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailId))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, intent.getStringExtra(Intent_AppName) + " (v-"+ intent.getStringExtra(Intent_AppVesion) + " -- "+ getString(R.string.report_bug_desc))
        startActivity(Intent.createChooser(mIntent, "Send Email"))
    }

}
