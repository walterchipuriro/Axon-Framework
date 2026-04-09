package walter.example.lab.config;

import org.axonframework.common.jdbc.UnitOfWorkAwareConnectionProviderWrapper;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class AxonEventStoreConfig {

    @Bean(name = "eventStoreDataSource")
    public DataSource eventStoreDataSource(
            @Value("${eventstore.datasource.url}") String url,
            @Value("${eventstore.datasource.driver-class-name}") String driverClassName,
            @Value("${eventstore.datasource.username}") String username,
            @Value("${eventstore.datasource.password}") String password
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "eventStoreSpringTxManager")
    public DataSourceTransactionManager eventStoreSpringTxManager(
            @Qualifier("eventStoreDataSource") DataSource dataSource
    ) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "eventStoreAxonTxManager")
    public TransactionManager eventStoreAxonTxManager(
            @Qualifier("eventStoreSpringTxManager") DataSourceTransactionManager txManager
    ) {
        return new SpringTransactionManager(txManager);
    }

    @Bean
    public EventStorageEngine eventStorageEngine(
            Serializer serializer,
            @Qualifier("eventStoreDataSource") DataSource dataSource,
            @Qualifier("eventStoreAxonTxManager") TransactionManager txManager
    ) {
        return JdbcEventStorageEngine.builder()
                .connectionProvider(new UnitOfWorkAwareConnectionProviderWrapper(dataSource::getConnection))
                .transactionManager(txManager)
                .snapshotSerializer(serializer)
                .eventSerializer(serializer)
                .build();
    }

    @Bean
    public EventStoreSchemaInitializer eventStoreSchemaInitializer(
            @Qualifier("eventStoreDataSource") DataSource dataSource
    ) {
        return new EventStoreSchemaInitializer(dataSource);
    }

    static class EventStoreSchemaInitializer {
        EventStoreSchemaInitializer(DataSource dataSource) {
            JdbcTemplate jdbc = new JdbcTemplate(dataSource);
            List<String> statements = List.of(
                    "CREATE TABLE IF NOT EXISTS DomainEventEntry (" +
                            "globalIndex BIGINT NOT NULL AUTO_INCREMENT, " +
                            "eventIdentifier VARCHAR(255) NOT NULL, " +
                            "metaData BLOB, " +
                            "payload BLOB NOT NULL, " +
                            "payloadRevision VARCHAR(255), " +
                            "payloadType VARCHAR(255) NOT NULL, " +
                            "timeStamp VARCHAR(255) NOT NULL, " +
                            "aggregateIdentifier VARCHAR(255) NOT NULL, " +
                            "sequenceNumber BIGINT NOT NULL, " +
                            "type VARCHAR(255), " +
                            "PRIMARY KEY (globalIndex), " +
                            "UNIQUE KEY uk_domain_event_entry_event_identifier (eventIdentifier), " +
                            "UNIQUE KEY uk_domain_event_entry_aggregate_seq (aggregateIdentifier, sequenceNumber, type)" +
                            ") ENGINE=InnoDB"
            );
            statements.forEach(jdbc::execute);

            // Migrate old snake_case columns if tables were previously created incorrectly.
            executeIgnoreError(jdbc, "ALTER TABLE DomainEventEntry CHANGE COLUMN global_index globalIndex BIGINT NOT NULL AUTO_INCREMENT");
            executeIgnoreError(jdbc, "ALTER TABLE DomainEventEntry CHANGE COLUMN event_identifier eventIdentifier VARCHAR(255) NOT NULL");
            executeIgnoreError(jdbc, "ALTER TABLE DomainEventEntry CHANGE COLUMN meta_data metaData BLOB");
            executeIgnoreError(jdbc, "ALTER TABLE DomainEventEntry CHANGE COLUMN payload_revision payloadRevision VARCHAR(255)");
            executeIgnoreError(jdbc, "ALTER TABLE DomainEventEntry CHANGE COLUMN payload_type payloadType VARCHAR(255) NOT NULL");
            executeIgnoreError(jdbc, "ALTER TABLE DomainEventEntry CHANGE COLUMN time_stamp timeStamp VARCHAR(255) NOT NULL");
            executeIgnoreError(jdbc, "ALTER TABLE DomainEventEntry CHANGE COLUMN aggregate_identifier aggregateIdentifier VARCHAR(255) NOT NULL");
            executeIgnoreError(jdbc, "ALTER TABLE DomainEventEntry CHANGE COLUMN sequence_number sequenceNumber BIGINT NOT NULL");

            // Ensure only the event table remains in the event store database.
            executeIgnoreError(jdbc, "DROP TABLE IF EXISTS SnapshotEventEntry");
            executeIgnoreError(jdbc, "DROP TABLE IF EXISTS TokenEntry");
            executeIgnoreError(jdbc, "DROP TABLE IF EXISTS patients");
        }

        private void executeIgnoreError(JdbcTemplate jdbc, String sql) {
            try {
                jdbc.execute(sql);
            } catch (Exception ignored) {
                // Ignore when column already migrated or doesn't exist.
            }
        }
    }
}
