package com.lethalmaus.exampleandroidproject.rules

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.IOException

class TextSuggestionsRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                disableSuggestions()
                try {
                    base.evaluate()
                } finally {
                    enableSuggestions()
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun disableSuggestions() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .executeShellCommand(String.format(AUTOFILL_SERVICE, "null"))
    }

    @Throws(IOException::class)
    private fun enableSuggestions() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .executeShellCommand(String.format(AUTOFILL_SERVICE, "com.google.android.gms/com.google.android.gms.autofill.service.AutofillService"))
    }

    companion object {
        private const val AUTOFILL_SERVICE = "settings put secure autofill_service %s"
    }
}