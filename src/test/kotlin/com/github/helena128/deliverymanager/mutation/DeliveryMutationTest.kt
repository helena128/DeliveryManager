package com.github.helena128.deliverymanager.mutation

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
class DeliveryMutationTest(@Autowired private val testClient: WebTestClient) {

    @TestConfiguration
    class DeliveryQueryTest {
        @Bean
        fun deliveryRepository(): DeliveryRepository = DeliveryRepositoryMockImpl()
    }

    @Test
    fun `verify processWidget query`() {
        val query = "markDeliveryReceived"
        val pendingEntity = DataHelper.findDeliveriesByStatus(DeliveryStatus.PENDING).first()
        val bodyValue = """
            mutation {
            	$query(id: "${pendingEntity.deliveryId}") {
                    deliveryId
                    deliveryStatus
                    product
              }
            }
        """.trimIndent()

        testClient.post()
            .uri(GRAPHQL_ENDPOINT)
            .accept(APPLICATION_JSON)
            .contentType(GRAPHQL_MEDIA_TYPE)
            .bodyValue(bodyValue)
            .exchange()
            .expectBody()
            .consumeWith(System.out::println)
            .jsonPath("$.data").exists()
            .jsonPath("$.data.$query").exists()
            .jsonPath("$.data.$query.deliveryId").isEqualTo(pendingEntity.deliveryId)
            .jsonPath("$.data.$query.deliveryStatus").isEqualTo(DeliveryStatus.RECEIVED.name)
            .jsonPath("$.data.$query.product").isEqualTo(pendingEntity.product)
            .jsonPath("$.data.$query.expectedDate").doesNotExist()
    }
}