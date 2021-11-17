package com.github.helena128.deliverymanager.util

import org.springframework.stereotype.Component
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Component
class DateMapper {
    fun convertToInstant(offsetDateTime: OffsetDateTime?): Instant? =
        if (offsetDateTime == null) null else offsetDateTime.toInstant()

    fun convertToOffsetDateTime(instant: Instant?): OffsetDateTime? =
        if (instant == null) null else instant.atOffset(ZoneOffset.UTC)
}