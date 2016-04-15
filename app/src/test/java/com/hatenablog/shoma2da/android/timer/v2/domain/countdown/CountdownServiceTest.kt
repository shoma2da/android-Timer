package com.hatenablog.shoma2da.android.timer.v2.domain.countdown

import android.content.Intent
import org.junit.Assert
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.Mockito.mock

class CountdownServiceTest {

    @Test
    fun onBindは使用しない() {
        val service = CountdownService()
        val result = service.onBind(mock(Intent::class.java))
        assertNull(result)
    }

}