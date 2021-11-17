package com.github.helena128.deliverymanager.entity

import com.github.helena128.deliverymanager.model.DeliveryStatus
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
data class DeliveryEntity (
    @Id
    val deliveryId: String,
    val product: String,
    val supplier: String,
    val quantity: Long = 0,
    val expectedDate: Instant,
    val expectedWarehouse: String,
    var deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING, // TODO: make val!
    val receivedDate: Instant? = null
)