package info.m2sj.springbatchkotlin.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class DatasourceConfig {
    @Primary
    @Bean(name = ["db1"])
    @ConfigurationProperties(prefix = "spring.db1.datasource")
    fun db1(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean(name = ["db2"])
    @ConfigurationProperties(prefix = "spring.db2.datasource")
    fun db2(): DataSource {
        return DataSourceBuilder.create().build()
    }
}