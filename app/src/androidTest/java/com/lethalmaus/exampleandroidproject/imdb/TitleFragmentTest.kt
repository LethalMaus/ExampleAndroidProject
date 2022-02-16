package com.lethalmaus.exampleandroidproject.imdb

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lethalmaus.exampleandroidproject.BaseTest
import com.lethalmaus.exampleandroidproject.CustomMatchers.withDrawable
import com.lethalmaus.exampleandroidproject.R
import com.lethalmaus.exampleandroidproject.dispatcher.IMDBDispatcher
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TitleFragmentTest : BaseTest() {

    @Before
    fun setup() {
        mockWebServer?.dispatcher = IMDBDispatcher()
        TitleManager.getTitles(FAVOURITES)?.forEach { title ->
            TitleManager.remove(title, FAVOURITES)
        }
        TitleManager.getTitles(HIDDEN)?.forEach { title ->
            TitleManager.remove(title, HIDDEN)
        }
    }

    @Test
    fun testTitleFavorites() {
        onView(withId(R.id.hidden))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(withDrawable(R.drawable.hide, R.color.teal_700)))
        onView(withId(R.id.noFavourites))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.no_favourites)))
        onView(withId(R.id.search))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(withDrawable(R.drawable.search, R.color.teal_700)))
            .perform(click())

        onView(withId(R.id.back))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(withDrawable(R.drawable.arrow, R.color.teal_700)))
        onView(withId(R.id.noSearchResults))
            .check(matches(not((isDisplayed()))))
        onView(withId(R.id.search))
            .check(matches(isDisplayed()))
            .check(matches(isClickable()))
            .check(matches(withHint(R.string.search)))
            .perform(click())
            .perform(typeText("red"))
            .perform(closeSoftKeyboard())
            .check(matches(withText("red")))
        onView(withId(R.id.noSearchResults))
            .check(matches(not((isDisplayed()))))

//        onView(withId(R.id.noSearchResults))
//            .check(matches(isDisplayed()))
//            .check(matches(withText(R.string.no_search_results)))
    }
}