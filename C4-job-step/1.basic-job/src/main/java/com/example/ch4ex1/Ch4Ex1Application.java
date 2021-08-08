package com.example.ch4ex1;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class Ch4Ex1Application {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job job() {
		return this.jobBuilderFactory
			.get("helloWorldJob")
			.incrementer(new RunIdIncrementer())
			.start(step1())
			.build();

	}

	@Bean
	public Step step1() {
		return this.stepBuilderFactory
			.get("step1")
			.tasklet((contribution, chunkContext) -> {
				System.out.println("Hello World!");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(Ch4Ex1Application.class, args);
	}
}