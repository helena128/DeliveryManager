package com.github.helena128.deliverymanager.util

import org.slf4j.LoggerFactory

fun <T> loggerFor(clazz: Class<T>) = LoggerFactory.getLogger(clazz)

object LoggingContants {
    const val INIT_DB_STARTED = "init-db-started"
    const val INIT_DB_FINISHED = "init-db-finished"
    const val SUCCESS_UPDATED = "success-updated"
    const val DELIVERY_FOUND = "delivery-found"
    const val CURRENT_STATUS_FILTER = "filter-current-status"
    const val DELIVERY_SAVED = "delivery-saved"
    const val GET_STARTED = "get-started"
    const val GET_FINISHED = "get-finished"
}