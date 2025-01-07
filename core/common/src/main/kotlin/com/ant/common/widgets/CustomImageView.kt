
package com.ant.common.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.EXACTLY
import androidx.appcompat.widget.AppCompatImageView

class CustomImageView(
    context: Context,
    attrs: AttributeSet?
) : AppCompatImageView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) * 3 / 2, EXACTLY)
        super.onMeasure(widthMeasureSpec, height)
    }
}