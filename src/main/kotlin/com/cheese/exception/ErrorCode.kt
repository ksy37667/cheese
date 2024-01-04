package com.cheese.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String
) {
    ERR_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버와의 통신이 원활하지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다."),
}


