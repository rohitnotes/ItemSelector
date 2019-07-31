package com.tonyyang.common.itemselector

fun String?.parcel(): String = this ?: ""

fun Double?.parcel(): Double = this ?: 0.0

fun Float?.parcel(): Float = this ?: 0F

fun Long?.parcel(): Long = this ?: 0

fun Int?.parcel(): Int = this ?: 0

fun Boolean?.parcel(): Boolean = this ?: false
