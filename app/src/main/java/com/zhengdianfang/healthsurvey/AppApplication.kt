package com.zhengdianfang.healthsurvey

import android.app.Application
import android.net.http.HttpResponseCache.install
import io.realm.Realm
import me.yokeyword.fragmentation.Fragmentation
import io.realm.RealmConfiguration



/**
 * Created by dfgzheng on 05/04/2018.
 */
class AppApplication: Application() {
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
}