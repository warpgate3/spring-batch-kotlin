package info.m2sj.springbatchkotlin

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableBatchProcessing
class SpringBatchKotlinApplication

fun main(args: Array<String>) {
    runApplication<SpringBatchKotlinApplication>(*args)
}
