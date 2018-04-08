package com.zhengdianfang.healthsurvey

import android.app.Application
import me.yokeyword.fragmentation.Fragmentation


/**
 * Created by dfgzheng on 05/04/2018.
 */
class AppApplication: Application() {

    val surveyStatusCache = mutableMapOf<String, Boolean>()

    override fun onCreate() {
        super.onCreate()

        Fragmentation.builder()
                // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .install()

    }

    override fun onTerminate() {
        super.onTerminate()
        surveyStatusCache.clear()
    }
}