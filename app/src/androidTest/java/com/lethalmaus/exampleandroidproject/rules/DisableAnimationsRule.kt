package com.lethalmaus.exampleandroidproject.rules

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.IOException

class DisableAnimationsRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                disableAnimations()
                try {
                    base.evaluate()
                } finally {
                    enableAnimations()
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun disableAnimations() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .executeShellCommand(String.format(TRANSITION_ANIMATION_SCALE, 0))
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .executeShellCommand(String.format(WINDOW_ANIMATION_SCALE, 0))
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .executeShellCommand(String.format(ANIMATOR_DURATION_SCALE, 0))
    }

    @Throws(IOException::class)
    private fun enableAnimations() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .executeShellCommand(String.format(TRANSITION_ANIMATION_SCALE, 1))
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .executeShellCommand(String.format(WINDOW_ANIMATION_SCALE, 1))
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .executeShellCommand(String.format(ANIMATOR_DURATION_SCALE, 1))
    }

    companion object {
        private const val TRANSITION_ANIMATION_SCALE = "settings put global transition_animation_scale %d"
        private const val WINDOW_ANIMATION_SCALE = "settings put global window_animation_scale %d"
        private const val ANIMATOR_DURATION_SCALE = "settings put global animator_duration_scale %d"
    }
}