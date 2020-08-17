package com.piloterr.android.piloterr.extensions

data class Optional<T>(val value: T?) {
    val isEmpty = value == null
}
fun <T> T?.asOptional() = Optional(this)