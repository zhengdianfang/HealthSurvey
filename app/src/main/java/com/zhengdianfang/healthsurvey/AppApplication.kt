package com.zhengdianfang.healthsurvey

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
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

        Realm.init(this)
        val config = RealmConfiguration.Builder().name("survey.realm").build()
        Realm.setDefaultConfiguration(config)
    }

    override fun onTerminate() {
        super.onTerminate()
        surveyStatusCache.clear()
    }
}