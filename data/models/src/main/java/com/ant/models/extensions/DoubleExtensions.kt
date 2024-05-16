package com.ant.models.extensions

fun Double.toTwoDigitNumber(): String {
    return String.format("%.1f", this);
}