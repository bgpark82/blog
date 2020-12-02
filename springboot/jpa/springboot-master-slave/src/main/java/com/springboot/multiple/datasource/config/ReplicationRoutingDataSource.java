package com.springboot.multiple.datasource.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    /**
     * DataSource를 key별로 담는다
     * @Transaction(readOnly=true)인 경우 "slave"를 key로 가진 DataSource를 이용한다
     * @see DataSourceConfig#routingDataSource
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                ? "slave" : "master";
    }
}
