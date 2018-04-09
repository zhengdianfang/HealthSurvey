package com.zhengdianfang.healthsurvey.views

import android.app.Activity
import android.os.Bundle
import android.widget.FrameLayout
import com.just.agentweb.AgentWeb
import com.zhengdianfang.healthsurvey.R


class WebActivity : Activity() {

    private var mAgentWeb: AgentWeb? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        this.mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(findViewById(R.id.frameLayout), FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(intent.getStringExtra("url"))
    }


    override fun onPause() {
        mAgentWeb?.getWebLifeCycle()?.onPause()
        super.onPause()

    }

    override fun onResume() {
        mAgentWeb?.getWebLifeCycle()?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb?.getWebLifeCycle()?.onDestroy()
    }
}
