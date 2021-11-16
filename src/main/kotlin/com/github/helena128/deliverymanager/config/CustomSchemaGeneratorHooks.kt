package com.github.helena128.deliverymanager.config

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLType
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubclassOf

@Configuration
class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {

    /**
     * Register additional GraphQL scalar types.
     */
    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier) {
        OffsetDateTime::class -> ExtendedScalars.DateTime
        Long::class -> ExtendedScalars.GraphQLLong
        else -> super.willGenerateGraphQLType(type)
    }

    /**
     * Register Reactor Mono monad type.
     */
    override fun willResolveMonad(type: KType): KType = when (type.classifier) {
        Mono::class -> type.arguments.first().type ?: type
        Set::class -> List::class.createType(type.arguments)
        else -> type
    }

    /**
     * Exclude the Spring bean factory interface
     */
    override fun isValidSuperclass(kClass: KClass<*>): Boolean {
        return when {
            kClass.isSubclassOf(BeanFactoryAware::class) -> false
            else -> super.isValidSuperclass(kClass)
        }
    }
}
