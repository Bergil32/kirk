package com.automation.remarks.kirk

import com.automation.remarks.kirk.Browser.Companion.getDriver
import com.automation.remarks.kirk.core.IBrowser
import com.automation.remarks.kirk.core.JsExecutor
import com.automation.remarks.kirk.core.ScreenshotContainer
import com.automation.remarks.kirk.core.WebDriverFactory
import org.aeonbits.owner.ConfigFactory.create
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

/**
 * Created by serg private fun isAbsoluteUrl(url: String): Boolean {
return (url.startsWith("http://") || url.startsWith("https://"))
}ey on 24.06.17.
 */

open class Browser(val driver: WebDriver = getDriver()) : IBrowser {

    private val navigator = Navigator(driver)

    companion object {

        private val driverFactory = WebDriverFactory()

        fun getDriver(): WebDriver {
            return driverFactory.getDriver()
        }

        fun setDriver(driver: WebDriver) {
            driverFactory.setWebDriver(driver)
        }

        fun getConfig(): Configuration {
            return create(Configuration::class.java,
                    System.getProperties())
        }

        fun drive(closure: Browser.() -> Unit) {
            Browser(getDriver()).apply(closure)
        }
    }

    override fun to(url: String) {
        navigator.to(url)
    }

    fun <T : Page> to(pageClass: () -> T): T {
        val page = pageClass()
        page.browser = this
        page.url?.let { to(it) }
        return page
    }

    fun <T : Page> to(pageClass: () -> T, closure: T.() -> Unit): Navigator {
        val page = to(pageClass)
        page.closure()
        return navigator
    }

    override fun element(byCss: String): KElement {
        return element(By.cssSelector(byCss))
    }

    override fun element(by: By): KElement {
        return KElement(by, driver)
    }

    override fun all(byCss: String): KElementCollection {
        return all(By.cssSelector(byCss))
    }

    override fun all(by: By): KElementCollection {
        return KElementCollection(by, driver)
    }

    override val currentUrl: String by lazy {
        driver.currentUrl
    }

    override val js: JsExecutor = JsExecutor(driver)

    fun takeScreenshot(saveTo: String = "${System.getProperty("user.dir")}/build/screen_${System.currentTimeMillis()}.png") {
        ScreenshotContainer(driver, saveTo).takeScreenshotAsFile()
    }
}