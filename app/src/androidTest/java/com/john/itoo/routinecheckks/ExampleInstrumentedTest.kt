package com.john.itoo.routinecheckks

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.screenshot.ScreenCapture


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy
import java.lang.Compiler.disable
import org.junit.AfterClass
import java.lang.Compiler.enable
import org.junit.BeforeClass





/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Before
    fun check() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())
    }


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.john.itoo.routinecheckks", appContext.packageName)
    }

    @Test
    fun testTakeScreenshot() {

        Screengrab.screenshot("before_button_click")

        // Your custom onView...
        onView(withId(R.id.upNext)).perform(click())

        Screengrab.screenshot("after_button_click")


    }

}
