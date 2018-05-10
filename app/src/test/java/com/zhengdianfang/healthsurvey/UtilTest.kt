package com.zhengdianfang.healthsurvey

import org.junit.Assert.*
import org.junit.Test

class UtilTest {
    @Test
    fun testGetUnquieId() {
        val mockPhone = "12543434567"
        println(Util.createUnqueId(mockPhone))
        println(Util.unquieIdIncrease())
        println(Util.unquieIdIncrease())
        println(Util.unquieIdIncrease())
        println(Util.createUnqueId(mockPhone))
//        assertEquals(Util.getUnquieId(mockPhone), "")
    }
}