package com.deepak.mytaxi

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test

class MainActivityTest {


    @Test
    fun testCheckViewsDisplay() {
        launch(MainActivity::class.java)
        onView(withId(R.id.fragmentcontainer))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.bottomnavigationview))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}