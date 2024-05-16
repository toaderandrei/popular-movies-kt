package com.ant.epoxy

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Dimension
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView

@Deprecated("todo: remove it if not needed")
@ModelView(saveViewState = true, autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TiviCarousel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : Carousel(context, attrs, defStyle) {

    @Dimension(unit = Dimension.PX)
    @set:ModelProp
    var itemWidth: Int = 0

    override fun onChildAttachedToWindow(child: View) {
        check(!(itemWidth > 0 && numViewsToShowOnScreen > 0)) {
            "Can't use itemWidth and numViewsToShowOnScreen together"
        }
        if (itemWidth > 0) {
            val childLayoutParams = child.layoutParams
            childLayoutParams.width = itemWidth
        }
        super.onChildAttachedToWindow(child)
    }
}
