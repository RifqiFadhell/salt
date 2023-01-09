package id.fadhell.salttest.ui

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt


fun dpToPx(resources: Resources, dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    ).roundToInt()
}