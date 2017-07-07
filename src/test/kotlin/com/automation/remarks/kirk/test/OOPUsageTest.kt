package com.automation.remarks.kirk.test

import com.automation.remarks.kirk.Browser
import com.automation.remarks.kirk.BrowserHandler
import com.automation.remarks.kirk.conditions.be
import com.automation.remarks.kirk.conditions.have
import com.automation.remarks.kirk.test.examples.simple.pages.StartPage
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.testng.annotations.Test

/**
 * Created by sergey on 28.06.17.
 */
class OOPUsageTest : BaseTest() {

    @Test
    fun testСanRunBrowser() {
        val browser = BrowserHandler(ChromeDriver())
        browser.to(url)
        browser.element("#header")
                .should(have.text("Kirk"))
    }

    @Test
    fun testCanSetNewDriver() {
        BrowserHandler(FirefoxDriver())
                .to(url)
    }

    @Test
    fun testCanOpenTwoBrowser() {
        Browser.drive {
            to(::StartPage) {
                header.should(be.visible)
            }
        }

        val ff = BrowserHandler(FirefoxDriver())
        ff.to(url)
        ff.all("li").should(have.size(3))
    }
}