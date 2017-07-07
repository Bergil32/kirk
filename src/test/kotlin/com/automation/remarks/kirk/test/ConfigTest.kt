package com.automation.remarks.kirk.test

import com.automation.remarks.kirk.Browser
import com.automation.remarks.kirk.conditions.have
import me.tatarka.assertk.assert
import me.tatarka.assertk.assertAll
import me.tatarka.assertk.assertions.hasClass
import me.tatarka.assertk.assertions.hasMessageContaining
import me.tatarka.assertk.assertions.isEqualTo
import org.openqa.selenium.TimeoutException
import org.testng.annotations.AfterMethod
import org.testng.annotations.Test

/**
 * Created by sergey on 27.06.17.
 */
class ConfigTest : BaseTest() {

    @AfterMethod
    fun tearDown() {
        System.clearProperty("browserName")
        System.clearProperty("timeout")
        System.clearProperty("startMaximized")
        System.clearProperty("baseUrl")
        System.clearProperty("screenSize")
        System.clearProperty("timeout")
        System.clearProperty("poolingInterval")
    }

    @Test
    fun testCheckDefaultConfigLoaded() {
        val cfg = Browser.getConfig()
        assertAll {
            assert(cfg.timeout()).isEqualTo(4000)
            assert(cfg.startMaximized())
        }
    }

    @Test
    fun testCanOverrideDefaultConfig() {
        System.setProperty("browserName", "firefox")
        System.setProperty("timeout", "6000")
        System.setProperty("startMaximized", "false")
        val cfg = Browser.getConfig()
        assertAll {
            assert(cfg.timeout()).isEqualTo(6000)
            assert(cfg.startMaximized()).isEqualTo(false)
        }
    }

    @Test fun testBrowserCanOpenCanonicalUrl() {
        System.setProperty("baseUrl", url.removeSuffix("/"))
        Browser.drive {
            to("")
            element("#header").should(have.text("Kirk"))
        }
    }

    @Test fun testBrowserWindowSize() {
        System.setProperty("startMaximized", "false")
        System.setProperty("screenSize", "640,480")
        Browser.drive {
            to(url)
            element("#header").should(have.text("Kirk"))
            val size = driver.manage().window().size
            assert(listOf(size.width, size.height)).isEqualTo(listOf(640, 480))
        }
    }

    @Test fun testCanOverrideTimeoutTime() {
        System.setProperty("timeout", "2000")
        System.setProperty("poolingInterval", "0.2")
        assert {
            Browser.drive {
                to(url)
                element("#header").should(have.text("irk"))
            }
        }.throwsError {
            it.hasClass(TimeoutException::class)
            it.hasMessageContaining("failed while waiting 2 seconds\n" +
                    "            to assert text\n" +
                    "            for element located {By.cssSelector: #header}\n" +
                    "            reason: condition did not match")
        }
    }


}