package com.cheese.exception

open class WebtoonExcetpion(
    val code: ErrorCode,
    override val message: String? = code.message
) : RuntimeException(message)

