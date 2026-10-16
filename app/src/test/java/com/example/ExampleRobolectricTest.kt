package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.RedeemRepository
import com.example.model.RedeemCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Daily Redeem Codes", appName)
    }

    @Test
    fun `repository has initial codes from user specification`() {
        val repo = RedeemRepository()
        val codes = repo.codes.value
        assertTrue(codes.size >= 4)
        
        // Verify key codes from prompt are present
        assertNotNull(codes.find { it.code == "FF9M22MCE8SW" })
        assertNotNull(codes.find { it.code == "FFBCJVGYGG82" })
        assertNotNull(codes.find { it.code == "3IBBMSL7AK8G" })
        assertNotNull(codes.find { it.code == "FF11NJN5YS3E" })
    }

    @Test
    fun `record copy increments count`() {
        val repo = RedeemRepository()
        val initialCount = repo.codes.value.first().copyCount
        val id = repo.codes.value.first().id
        repo.recordCopied(id)
        val newCount = repo.codes.value.first { it.id == id }.copyCount
        assertEquals(initialCount + 1, newCount)
    }

    @Test
    fun `toggle favorite works`() {
        val repo = RedeemRepository()
        val id = repo.codes.value.first().id
        repo.toggleFavorite(id)
        assertTrue(repo.codes.value.first { it.id == id }.isFavorite)
    }
}
