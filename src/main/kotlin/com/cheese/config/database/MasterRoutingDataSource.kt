package com.cheese.config.database

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class MasterRoutingDataSource : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any {
//        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
//            return DBType.SLAVE
//        }
        return DBType.MASTER
    }
}

enum class DBType {
    MASTER, SLAVE
}