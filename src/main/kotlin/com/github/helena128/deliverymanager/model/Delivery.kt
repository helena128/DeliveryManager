package com.github.helena128.deliverymanager.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import java.time.OffsetDateTime

@GraphQLDescription("Captures main information about delivery")
data class Delivery(
    val deliveryId: ID,
    val product: String,
    val supplier: String,
    val quantity: Long = 0,
    val expectedDate: OffsetDateTime,
    val expectedWarehouse: String,
    val deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING,
    val updatedDate: OffsetDateTime? = null
)