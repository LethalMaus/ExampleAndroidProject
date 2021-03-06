package com.lethalmaus.exampleandroidproject

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

object CustomMatchers {

    fun withDrawable(resourceId: Int, color: Int? = null): Matcher<View> {
        return object : BoundedMatcher<View, View>(View::class.java) {

            private var resourceName: String? = resourceId.toString()
            private var viewName: String? = null

            override fun describeTo(description: Description) {
                description.appendText("$viewName has image drawable resource $resourceName")
            }

            public override fun matchesSafely(target: View): Boolean {
                val resources = target.context.resources
                resourceName = resources.getResourceEntryName(resourceId)
                viewName = resources.getResourceName(target.id)

                if (target is ImageView) {
                    return handleImageView(target, resourceId, color)
                } else if (target is TextView || target is ConstraintLayout) {
                    handleTextOrConstraint(target, resourceId)
                }
                return false
            }
        }
    }

    private fun handleImageView(target: ImageView, resourceId: Int, color: Int? = null): Boolean {
        val resources = target.context.resources
        val expectedDrawable = resources.getDrawable(resourceId, null) ?: return false
        if (resourceId == -1) {
            return target.drawable == null
        }
        if (resourceId == -2) {
            return target.drawable != null
        }

        expectedDrawable.colorFilter = target.drawable.colorFilter
        color?.let {
            DrawableCompat.setTint(
                DrawableCompat.wrap(expectedDrawable),
                ContextCompat.getColor(target.context, it)
            )
        }

        val bitmap = getBitmap(target.drawable)
        val otherBitmap = getBitmap(expectedDrawable)

        if (target.isVisible && target.drawable.colorFilter != null && !bitmap.sameAs(otherBitmap)) {
            val resourceName = resources.getResourceEntryName(resourceId)
            val viewName = resources.getResourceName(target.id)
            Log.e("ViewInteraction","$viewName has a color filter & may not match $resourceName but is visible. Please check manually")
            return true
        }
        return bitmap.sameAs(otherBitmap)
    }

    private fun handleTextOrConstraint(target: View, resourceId: Int): Boolean {
        val targetState = target.background.constantState
        val expectedState = ContextCompat.getDrawable(target.context, resourceId)!!.constantState!!
        return targetState == expectedState
    }

    private fun getBitmap(drawable: Drawable): Bitmap {
        val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 100
        val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 100
        val bitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}