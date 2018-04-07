package com.zhengdianfang.healthsurvey

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by dfgzheng on 05/04/2018.
 */
class Des4Test{
    @Test
    fun testEncode() {
        Des4.encode("")
    }

    @Test
    fun testDecode() {
        val decode = Des4.decode("34xE8SeOPF6R3DTb0tZnBw==")
        println(decode)
    }
}