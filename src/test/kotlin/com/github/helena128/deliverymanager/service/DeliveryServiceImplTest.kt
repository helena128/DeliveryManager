package com.github.helena128.deliverymanager.service

import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryRepository
import com.github.helena128.deliverymanager.util.DataHelper
import com.nhaarman.mockito_kotlin.times
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class DeliveryServiceImplTest {

    @Mock
    lateinit var repository: DeliveryRepository;

    @InjectMocks
    lateinit var deliveryService: DeliveryServiceImpl;

    @Test
    fun `Test Listing Not Received Items`() {
        val pendingDeliveries = DataHelper.findDeliveriesByStatus(DeliveryStatus.PENDING)
        Mockito.`when`(repository.findPendingDeliveries()).thenReturn(pendingDeliveries)
        val actualResult = deliveryService.getDeliveries(false)
        Assertions.assertEquals(pendingDeliveries, actualResult)
        Mockito.verify(repository, times(1)).findPendingDeliveries()
        Mockito.verify(repository, times(0)).findReceivedDeliveries()
    }

    @Test
    fun `Test Listing Received Items`() {
        val receivedDeliveries = DataHelper.findDeliveriesByStatus(DeliveryStatus.RECEIVED)
        Mockito.`when`(repository.findReceivedDeliveries()).thenReturn(receivedDeliveries)
        val actualResult = deliveryService.getDeliveries(true)
        Assertions.assertEquals(receivedDeliveries, actualResult)
        Mockito.verify(repository, times(0)).findPendingDeliveries()
        Mockito.verify(repository, times(1)).findReceivedDeliveries()
    }

    @Test
    fun `Test Receiving Empty List When No Items In System`() {
        Mockito.`when`(repository.findReceivedDeliveries()).thenReturn(emptyList())
        val actualResult = deliveryService.getDeliveries(true)
        Assertions.assertEquals(0, actualResult.size)
        Mockito.verify(repository, times(0)).findPendingDeliveries()
        Mockito.verify(repository, times(1)).findReceivedDeliveries()
    }

}