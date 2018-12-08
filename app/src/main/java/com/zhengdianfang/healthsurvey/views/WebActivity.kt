package com.zhengdianfang.healthsurvey.views

import android.app.Activity
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.FrameLayout
import com.just.agentweb.AgentWeb
import com.zhengdianfang.healthsurvey.R
import kotlinx.android.synthetic.main.tool_bar.*
import android.content.ClipData
import android.widget.Toast


class WebActivity : Activity() {

    private var mAgentWeb: AgentWeb? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val url = intent.getStringExtra("url")
        this.mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(findViewById(R.id.frameLayout), FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url)


        back.setOnClickListener {
            finish()
        }

        if (intent.getBooleanExtra("copy", false)) {
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(url, url)
            clipboardManager.primaryClip = clip
            Toast.makeText(this, "抽奖链接，复制成功", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()

    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb?.webLifeCycle?.onDestroy()
    }
}
