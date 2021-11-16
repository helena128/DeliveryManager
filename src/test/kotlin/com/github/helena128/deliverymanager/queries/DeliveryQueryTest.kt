package com.github.helena128.deliverymanager.queries

import com.github.helena128.deliverymanager.constants.GRAPHQL_ENDPOINT
import com.github.helena128.deliverymanager.constants.GRAPHQL_MEDIA_TYPE
import com.github.helena128.deliverymanager.model.DeliveryStatus
import com.github.helena128.deliverymanager.repository.DeliveryRepository
import com.github.helena128.deliverymanager.util.DataHelper
import com.github.helena128.deliverymanager.util.DeliveryRepositoryMockImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeliveryQueryTest(@Autowired private val testClient: WebTestClient) {

    @TestConfiguration
    class DeliveryQueryTest {
        @Bean
        fun deliveryRepository(): DeliveryRepository = DeliveryRepositoryMockImpl()
    }

    @Test
    fun `Test getting pending items`() {
        val query = """
              getDeliveries (received: false) {
                deliveryId
                product
              }
        """.trimIndent()

        val expectedData = DataHelper.findDeliveriesByStatus(DeliveryStatus.PENDING)

        testClient.post()
            .uri(GRAPHQL_ENDPOINT)
            .accept(APPLICATION_JSON)
            .contentType(GRAPHQL_MEDIA_TYPE)
            .bodyValue("query { $query }")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody()
            .jsonPath("$.data").exists()
            .jsonPath("$.data.getDeliveries").exists()
            .jsonPath("$.data.getDeliveries.[0].deliveryId").isEqualTo(expectedData.get(0).deliveryId.value)
            .jsonPath("$.data.getDeliveries.[0].product").isEqualTo(expectedData.get(0).product)
            .jsonPath("$.data.getDeliveries.[0].deliveryStatus").doesNotExist()
    }

    @Test
    fun `Test getting received items`() {
        val query = """
              getDeliveries (received: true) {
                deliveryId
                product
                deliveryStatus
              }
        """.trimIndent()

        val expectedData = DataHelper.findDeliveriesByStatus(DeliveryStatus.RECEIVED)

        testClient.post()
            .uri(GRAPHQL_ENDPOINT)
            .accept(APPLICATION_JSON)
            .contentType(GRAPHQL_MEDIA_TYPE)
            .bodyValue("query { $query }")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody()
            .jsonPath("$.data").exists()
            .jsonPath("$.data.getDeliveries").exists()
            .jsonPath("$.data.getDeliveries.[0].deliveryId").isEqualTo(expectedData.get(0).deliveryId.value)
            .jsonPath("$.data.getDeliveries.[0].product").isEqualTo(expectedData.get(0).product)
            .jsonPath("$.data.getDeliveries.[0].deliveryStatus").isEqualTo(DeliveryStatus.RECEIVED.name)
            .jsonPath("$.data.getDeliveries.[1].deliveryId").isEqualTo(expectedData.get(1).deliveryId.value)
            .jsonPath("$.data.getDeliveries.[1].product").isEqualTo(expectedData.get(1).product)
    }
}