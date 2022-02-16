package com.lethalmaus.exampleandroidproject

import androidx.test.core.app.ActivityScenario
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import com.lethalmaus.exampleandroidproject.rules.DisableAnimationsRule
import com.lethalmaus.exampleandroidproject.rules.TextSuggestionsRule
import org.junit.After
import org.junit.Before

open class BaseTest {

    @get:Rule
    var animationsRule = DisableAnimationsRule()
    @get:Rule
    var textSuggestionsRule = TextSuggestionsRule()

    lateinit var activity: MainActivity
    lateinit var scenario: ActivityScenario<MainActivity>

    var mockWebServer: MockWebServer? = null

    @Before
    fun launchActivity() {
        mockWebServer = MockWebServer()
        mockWebServer?.start(8185)
        mockWebServer?.url("/")
        scenario = ActivityScenario.launch(MainActivity::class.java).onActivity {
            activity = it
        }
    }

    @After
    open fun tearDown() {
        mockWebServer?.shutdown()
    }
}