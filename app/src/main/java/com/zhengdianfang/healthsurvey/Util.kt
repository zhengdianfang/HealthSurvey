package com.zhengdianfang.healthsurvey

import java.util.regex.Pattern

/**
 * Created by dfgzheng on 05/04/2018.
 */
object Util {
    /**
     * 手机号号段校验，
     * 第1位：1；
     * 第2位：{3、4、5、6、7、8}任意数字；
     * 第3—11位：0—9任意数字
     * @param value
     * @return
     */
    fun isTelPhoneNumber(value: String?): Boolean {
        if (value != null && value.length == 11) {
            val pattern = Pattern.compile("^1[3|4|5|6|7|8][0-9]\\d{8}$")
            val matcher = pattern.matcher(value)
            return matcher.matches()
        }
        return false
    }


    fun getUnquieid(phone: String): String {
        val time = System.currentTimeMillis().toString().substring(0, 10)
        return Des4.encode(phone + time)
    }
}