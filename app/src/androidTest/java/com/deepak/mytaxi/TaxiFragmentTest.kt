package com.deepak.mytaxi

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.deepak.mytaxi.ui.taxi.TaxiFragment
import com.deepak.mytaxi.utils.RVMatcher.atPositionOnView
import org.junit.Test

class TaxiFragmentTest {


    @Test
    fun taxiAvailable_shouldDisplay() {
        launchFragmentInContainer<TaxiFragment>(Bundle(), R.style.AppTheme)
        Espresso.onView(withId(R.id.vehicleView))
            .check(ViewAssertions.matches(isDisplayed()))
                Espresso.onView(withId(R.id.fleet_type)).check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.location)).check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.location_icon)).check(ViewAssertions.matches(
            isDisplayed()))
        Espresso.onView(withId(R.id.heading_icon)).check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.heading)).check(ViewAssertions.matches(isDisplayed()))

    }

    @Test
    fun isFleettype_display() {
        launchFragmentInContainer<TaxiFragment>(Bundle(), R.style.AppTheme)

        Espresso.onView(withId(R.id.vehicleView)).apply {
            check(ViewAssertions.matches(atPositionOnView(0, withText("TAXI"), R.id.fleet_type)))
                check(ViewAssertions.matches(atPositionOnView(0, withId(R.drawable.taxi), R.id.location_icon)))
        }
    }

}