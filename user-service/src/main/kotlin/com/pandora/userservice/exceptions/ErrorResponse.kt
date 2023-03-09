package com.pandora.userservice.exceptions

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.ZonedDateTime

class ErrorResponse(
    @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") var timestamp: ZonedDateTime,
    var statusCode: Int,
    var path: String,
    var message: String
)
