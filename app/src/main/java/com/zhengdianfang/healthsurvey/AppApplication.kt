package com.zhengdianfang.healthsurvey

import android.app.Application
import android.util.Log
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import me.yokeyword.fragmentation.Fragmentation


/**
 * Created by dfgzheng on 05/04/2018.
 */
class AppApplication: Application() {

    val surveyStatusCache = mutableMapOf<String, Boolean>()
    private var gloalUnquieId = ""

    override fun onCreate() {
        super.onCreate()

        Fragmentation.builder()
                // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .install()

        CrashReport.initCrashReport(this, "01ed834c99", true, null)
        CrashReport.setAppChannel(this, "zdf")
        CrashReport.setAppPackage(this, packageName)

    }

    override fun onTerminate() {
        super.onTerminate()
        surveyStatusCache.clear()
    }

    fun createUnqueId(phone: String): String {
        val time = System.currentTimeMillis().toString().substring(0, 10)
        gloalUnquieId = if (phone.isNullOrBlank()) {
            "00000000000$time"
        } else {
            phone + time
        }
        return gloalUnquieId
    }
    fun unquieIdIncrease(): String {
        val split = gloalUnquieId.split("_")
        gloalUnquieId = if (split.count() == 1) {
            "${split.first()}_1"
        } else {
            "${split.first()}_${split.last().toInt() + 1}"
        }
        return gloalUnquieId
    }
    fun resetUnquieId() {
        gloalUnquieId = gloalUnquieId.split("_").first()
    }

    fun getUnqueId(): String {
        Log.d("Util", "gloalUnquieId : $gloalUnquieId")
        return Des4.encode(gloalUnquieId)
    }
}