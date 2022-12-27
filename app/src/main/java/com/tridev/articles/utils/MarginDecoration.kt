package com.tridev.articles.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class MarginDecoration(context: Context, dimenRes: Int) : ItemDecoration() {
    private val margin: Int

    init {
        margin = context.resources.getDimensionPixelSize(dimenRes)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        outRect[margin, margin, margin] = margin
    }
}