package info.m2sj.springbatchkotlin.job

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.json.JacksonJsonObjectReader
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class TutorialJob(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    fun jobOfTutorial(): Job {
        return jobBuilderFactory.get("tutorialJob")
            .start(tutorialStep())
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    fun tutorialStep(): Step {
        return stepBuilderFactory.get("tutorialStep")
            .chunk<Box, Box>(1)
            .reader(reader())
            .writer(writer())
            .build()
    }

    @Bean
    fun writer(): ItemWriter<Box> {
        return ItemWriter<Box> {
            it.forEach { item -> println(item.name) }
        }
    }

    @Bean
    fun reader(): ItemReader<Box> {
        val r = JsonItemReaderBuilder<Box>().name("jsonItemReader")
            .resource(
                ClassPathResource("/tutorial.json")
            )
            .jsonObjectReader(JacksonJsonObjectReader(Box::class.java))
            .build()
        return r
    }
}

data class Box(val name: String?) {
    constructor() : this(null) {

    }
}