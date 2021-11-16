package com.github.helena128.deliverymanager.entity

import com.github.helena128.deliverymanager.model.DeliveryStatus
import java.time.OffsetDateTime

data class DeliveryEntity (
    val deliveryId: String,
    val product: String,
    val supplier: String,
    val quantity: Long = 0,
    val expectedDate: OffsetDateTime,
    val expectedWarehouse: String,
    val deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING,
    val receivedDate: OffsetDateTime? = null
)