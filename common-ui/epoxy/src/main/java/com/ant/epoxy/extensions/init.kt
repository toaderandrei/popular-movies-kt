package com.ant.epoxy.extensions

import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.ant.common.decorator.MarginItemDecoration

fun EpoxyRecyclerView.init(
    controller: EpoxyController,
    marginItemDecoration: MarginItemDecoration = MarginItemDecoration(
        defaultMargin = 120,
        verticalMargin = 24,
        firstItemMargin = 120,
        lastItemMargin = 120
    )
) {
    this.apply {
        setController(controller)
        addItemDecoration(
            marginItemDecoration
        )
    }
}