package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.entity.DeliveryEntity
import com.github.helena128.deliverymanager.model.Delivery
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface DeliveryMapper {

    @Mapping(target = "deliveryId", expression = "java(new com.expediagroup.graphql.generator.scalars.ID(deliveryEntity.getDeliveryId()))")
    fun convertToDto(deliveryEntity: DeliveryEntity): Delivery

    @Mapping(target = "deliveryId", expression = "java(delivery.getDeliveryId().getValue())")
    fun convertToEntity(delivery: Delivery): DeliveryEntity
}
