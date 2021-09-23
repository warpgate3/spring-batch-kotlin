package info.m2sj.springbatchkotlin.job

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.json.JacksonJsonObjectReader
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.sql.DataSource

@Configuration
class TutorialJob(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory,
    val dataSource: DataSource
) {

    @Bean
    fun db2dbJob(): Job {
        return jobBuilderFactory.get("json2DbJob")
            .start(db2dbStep())
            .incrementer(RunIdIncrementer())
            .build()
    }

    fun db2dbStep(): Step {
        return stepBuilderFactory.get("json2DbStep")
            .chunk<Box, Box>(1)
            .reader(json2dbReader())
            .writer(json2dbWriter())
            .build()
    }

    @Bean(name= ["json2dbWriter"])
    fun json2dbWriter(): JdbcBatchItemWriter<Box> {
        val jdbcBatchItemWriter = JdbcBatchItemWriterBuilder<Box>()
            .dataSource(dataSource)
            .sql("insert into test1 (name) values (:name)")
            .beanMapped()
            .build()

        return jdbcBatchItemWriter
    }

    @Bean(name= ["json2dbReader"])
    fun json2dbReader(): ItemReader<Box> {
        val r = JsonItemReaderBuilder<Box>().name("jsonItemReader")
            .resource(
                ClassPathResource("/tutorial.json")
            )
            .jsonObjectReader(JacksonJsonObjectReader(Box::class.java))
            .build()
        return r
    }
}