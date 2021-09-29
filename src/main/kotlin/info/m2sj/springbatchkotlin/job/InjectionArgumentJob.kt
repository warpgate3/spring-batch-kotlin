package info.m2sj.springbatchkotlin.job

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.CompositeJobParametersValidator
import org.springframework.batch.core.job.DefaultJobParametersValidator
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class InjectionArgumentJob(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun inJeArgJob(): Job {
        return jobBuilderFactory.get("inJeArgJob")
            .start(inJeArgStep(null))
            .validator(validator())
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    @JobScope
    fun inJeArgStep(@Value("#{jobParameters[name]}") name: String?): Step {
        return stepBuilderFactory.get("inJeArgStep")
            .chunk<String, String>(10)
            .reader(inJeArgReader(name))
            .writer(inJeArgWriter(name))
            .build()
    }

    @Bean(name = ["inJeArgWriter"])
    @StepScope
    fun inJeArgWriter(@Value("#{jobParameters[name]}") name: String?): ItemWriter<String> {
        return ItemWriter<String> {
            TimeUnit.SECONDS.sleep(1)
            println("name -> ${name}")
        }
    }

    @Bean(name = ["inJeArgReader"])
    @StepScope
    fun inJeArgReader(@Value("#{jobParameters[name]}") name: String?): ItemReader<String> {
        return ItemReader<String> {
            "[${name}]"
        }
    }

    @Bean(name = ["validator"])
    fun validator(): CompositeJobParametersValidator {
        val cvs = CompositeJobParametersValidator()

        val djv = DefaultJobParametersValidator(arrayOf("name"), arrayOf())

        djv.afterPropertiesSet()
        cvs.setValidators(listOf(djv))

        return cvs
    }
}