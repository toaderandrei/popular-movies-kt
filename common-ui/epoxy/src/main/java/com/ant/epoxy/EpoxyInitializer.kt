package com.ant.epoxy

import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyController
import com.ant.common.di.Initializer
import javax.inject.Inject

class EpoxyInitializer @Inject constructor() : Initializer {
    override fun init() {
        // TODO: verify this. Make EpoxyController diffing async by default
        val asyncHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()
        EpoxyController.defaultDiffingHandler = asyncHandler
    }
}
