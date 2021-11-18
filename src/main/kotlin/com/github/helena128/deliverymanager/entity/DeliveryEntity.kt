package com.github.helena128.deliverymanager.entity

import com.github.helena128.deliverymanager.model.DeliveryStatus
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table("delivery")
data class DeliveryEntity(
    @Id
    val id: Long? = null,
    @Column("delivery_id")
    val deliveryId: String,
    val product: String,
    val supplier: String,
    val quantity: Long = 0,
    @Column("expected_date")
    val expectedDate: OffsetDateTime,
    @Column("expected_warehouse")
    val expectedWarehouse: String,
    @Column("delivery_status")
    var deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING,
    @Column("received_date")
    var receivedDate: OffsetDateTime? = null
)