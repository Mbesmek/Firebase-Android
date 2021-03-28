package com.example.firebase101.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

object Utility {
    fun getColor(ctx: Context, @ColorRes resourceId: Int): Int {
        return ContextCompat.getColor(ctx, resourceId)
    }
}