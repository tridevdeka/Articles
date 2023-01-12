package com.tridev.articles.utils

import android.graphics.Color
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.tridev.articles.R

object MaterialMotionUtils {


    fun Fragment.exitTransition() {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_small).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_small).toLong()
        }
    }

    fun Fragment.applySharedElementTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHost_fragment_container
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }

        sharedElementReturnTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHost_fragment_container
            duration = resources.getInteger(R.integer.reply_motion_duration_small).toLong()
            scrimColor = Color.TRANSPARENT
        }
    }


    fun Fragment.postPoneTransition() {
        postponeEnterTransition()
        (requireView().parent as ViewGroup).viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
    }


}