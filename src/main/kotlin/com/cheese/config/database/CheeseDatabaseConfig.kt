package com.cheese.config.database

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.hibernate.cfg.AvailableSettings
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.orm.hibernate5.SpringBeanContainer
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.cheese.user.repository", "com.cheese.fiction.repository", "com.cheese.webtoon.repository"],
    entityManagerFactoryRef = "cheeseEntityManager",
    transactionManagerRef = "cheeseTransactionManager"
)
class CheeseDatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "cheese.master.datasource")
    fun cheeseMasterDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "cheese.master.datasource.hikari")
    fun cheeseMasterHikariDataSource(@Qualifier("cheeseMasterDataSourceProperties") masterProperty: DataSourceProperties): HikariDataSource {
        return masterProperty.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
    }

    @Bean
    fun cheeseRoutingDataSource(
        @Qualifier("cheeseMasterHikariDataSource") masterDataSource: DataSource
    ): DataSource {
        val dataSourceMap: Map<Any, Any> = hashMapOf(
            DBType.MASTER to masterDataSource
        )

        return LazyConnectionDataSourceProxy(MasterRoutingDataSource().apply {
            this.setDefaultTargetDataSource(masterDataSource)
            this.setTargetDataSources(dataSourceMap)
            this.afterPropertiesSet()
        })
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "cheese.jpa")
    fun cheeseJpaProperties(): JpaProperties {
        return JpaProperties()
    }

    @Bean
    @Primary
    fun cheeseEntityManager(
        builder: EntityManagerFactoryBuilder,
        beanFactory: ConfigurableListableBeanFactory,
        @Qualifier("cheeseRoutingDataSource") cheeseRoutingDataSource: DataSource
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(cheeseRoutingDataSource)
            .properties(mapOf(
                AvailableSettings.BEAN_CONTAINER to SpringBeanContainer(beanFactory)
            ))
            .packages(
                "com.cheese.fiction.entity", "com.cheese.user.entity", "com.cheese.webtoon.entity"
            )
            .build()
    }

    @Bean
    @Primary
    fun cheeseTransactionManager(@Qualifier("cheeseEntityManager") cheeseEntityManager: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(cheeseEntityManager)
    }
}