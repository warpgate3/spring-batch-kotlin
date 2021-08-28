package info.m2sj.springbatchkotlin.job

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.net.URL

@Configuration
class TutorialJob(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory
) {

    fun tutorialJob(): Job {
        return jobBuilderFactory.get("tutorialJob")
            .start(tutorialStep())
            .build()
    }

    private fun tutorialStep(): Step {
        return stepBuilderFactory.get("tutorialStep")
            .chunk<String, String>(100)
            .reader(reader())
            .writer(writer())
            .build()
    }

    private fun writer(): ItemWriter<String> {
        return ItemWriter<String> {
            println(">>>>${it}")
        }
    }

    private fun reader(): ItemReader<String> {
        return JsonItemReaderBuilder<String>().name("jsonItemReader")
            .resource(ClassPathResource("info/m2sj/springbatchkotlin/job/tutorial.json"))
            .build()
    }
}