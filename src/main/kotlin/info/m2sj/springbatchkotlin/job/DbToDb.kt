package info.m2sj.springbatchkotlin.job

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean
import org.springframework.batch.item.json.JacksonJsonObjectReader
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.sql.DataSource

@Configuration
class DbToDb(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory,
    @Qualifier(value = "db1") val db1: DataSource,
    @Qualifier(value = "db2") val db2: DataSource
) {

    @Bean
    fun db2dbJob(): Job {
        return jobBuilderFactory.get("db2dbJob")
            .start(db2dbStep())
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    fun db2dbStep(): Step {
        return stepBuilderFactory.get("db2dbStep")
            .chunk<Box, Box>(1)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()
    }

    @Bean(name= ["db2dbReader"])
    fun db2dbReader(): ItemReader<Box> {
        val queryProvider = SqlPagingQueryProviderFactoryBean()
        queryProvider.setDataSource(db1)
        queryProvider.setSelectClause("name")
        queryProvider.setFromClause("test1")
        queryProvider.setSortKey("name")

        return JdbcPagingItemReaderBuilder<Box>()
            .pageSize(1000)
            .fetchSize(1000)
            .dataSource(db1)
            .beanRowMapper(Box::class.java)
            .queryProvider(queryProvider.`object`)
            .name("itemReader")
            .build()
    }

    @Bean(name= ["db2dbProcessor"])
    fun db2dbProcessor(): ItemProcessor<Box, Box> {
       return ItemProcessor {
           it.name = "[" + it.name + "]"
           println(">>>>> ${it}")
           it
       }
    }

    @Bean(name= ["db2dbWriter"])
    fun db2dbWriter(): ItemWriter<Box> {
        return JdbcBatchItemWriterBuilder<Box>()
            .dataSource(db2)
            .sql("insert into test2(name) values (:name)")
            .beanMapped()
            .build()
    }
}

