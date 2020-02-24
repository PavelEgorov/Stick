package com.egorovsoft.stick

import android.content.Context
import androidx.core.content.ContextCompat
import com.egorovsoft.stick.data.Note

fun Note.Color.getColorInt(context: Context): Int =
    ContextCompat.getColor(
        context, when (this) {
            Note.Color.WHITE -> R.color.color_white
            Note.Color.YELLOW -> R.color.color_yello
            Note.Color.RED -> R.color.color_red
            Note.Color.GREEN -> R.color.color_green
            Note.Color.BLUE -> R.color.color_blue
            else -> R.color.color_green
        }
    )


fun Note.Color.getColorRes(): Int = when (this) {
    Note.Color.WHITE -> R.color.color_white
    Note.Color.YELLOW -> R.color.color_yello
    Note.Color.RED -> R.color.color_red
    Note.Color.GREEN -> R.color.color_green
    Note.Color.BLUE -> R.color.color_blue
    else -> R.color.color_green
}