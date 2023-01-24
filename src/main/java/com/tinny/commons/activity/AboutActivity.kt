package com.tinny.commons.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tinny.commons.*
import com.tinny.commons.databinding.ActivityAboutBinding
import com.tinny.commons.extentions.*
import com.tinny.commons.helper.CommonConstants
import com.tinny.commons.helper.CommonTextIcons
import java.util.*

class AboutActivity : AppCompatActivity(),View.OnClickListener {


    val emailId= "tinnymobileapps@gmail.com"
    private lateinit var binding: ActivityAboutBinding

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.moreApps ->{
               launchViewIntent("https://play.google.com/store/apps/developer?id=Tinnymobileapps")
//                launchViewIntent("https://play.google.com/store/apps/developer?id=Simple+Puzzle+games")

            }
            R.id.rateUs ->{launchViewIntent("market://details?id=$packageName")}
            R.id.reportBugs ->{reportBug()}
            R.id.share ->{onClickShare()}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        setContentView(R.layout.activity_about)


        intent.getStringExtra(Intent_AppName)
        binding.txtAppName.text = intent.getStringExtra(Intent_AppName)
        binding.imgApp.setImageResource(intent.getIntExtra(Intent_AppIcon,0))

        binding.moreApps.txtTitle.text = getString(R.string.more_apps)
        binding.moreApps.txtDesc.text = getString(R.string.more_apps_desc)

        binding.rateUs.txtTitle.text = getString(R.string.rate_us)
        binding.rateUs.txtDesc.text =getString(R.string.rate_us_desc)

        binding.share.txtTitle.text = getString(R.string.share)
        binding.share.txtDesc.text =getString(R.string.share_desc)

        binding.reportBugs.txtTitle.text = getString(R.string.report_bug)
        binding.reportBugs.txtDesc.text =getString(R.string.report_bug_desc)
        setupCopyright()

        if (intent.getBooleanExtra(Intent_Translation,true)){
            binding.rrTrans.makeVisible()
        }else{
            binding.rrTrans.makeGone()
        }

        binding.moreApps.txtIcon.setFontWithColor(this, getString(R.string.f_more_apps), CommonConstants.icomoonCommon, resources.getColor(R.color.md_grey_black))
        binding.rateUs.txtIcon.setFontWithColor(this, getString(R.string.f_full_star), CommonConstants.icomoonCommon, resources.getColor(R.color.md_grey_black))
        binding.share.txtIcon.setFontWithColor(this, getString(R.string.f_fav), CommonConstants.icomoonCommon, resources.getColor(R.color.md_grey_black))
        binding.reportBugs.txtIcon.setFontWithColor(this, getString(R.string.f_bug), CommonConstants.icomoonCommon, resources.getColor(R.color.md_grey_black))
        binding.txtIconTran.setFontWithColor(this, CommonTextIcons.F_Translation, CommonConstants.icomoonCommon, resources.getColor(R.color.md_grey_black))



        binding.txtTranHelp.setOnClickListener {
            val mIntent = Intent(Intent.ACTION_SENDTO)
            mIntent.data = Uri.parse("mailto:")
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailId))
            mIntent.putExtra(Intent.EXTRA_SUBJECT, intent.getStringExtra(Intent_AppName) + " (v-"+ intent.getStringExtra(Intent_AppVesion) + " -- "+ "Helping in text translation")
            startActivity(Intent.createChooser(mIntent, "Send Email"))
        }
//        Toast.makeText(this,packageName,Toast.LENGTH_SHORT).show()

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
        binding.txtCopyrights.text = String.format(getString(R.string.copyright1), versionName, year)
    }

    private fun reportBug(){
        val mIntent = Intent(Intent.ACTION_SENDTO)
        mIntent.data = Uri.parse("mailto:")
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailId))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, intent.getStringExtra(Intent_AppName) + " (v-"+ intent.getStringExtra(Intent_AppVesion) + " -- "+ getString(R.string.report_bug_desc))
        startActivity(Intent.createChooser(mIntent, "Send Email"))
    }

}
