package com.example.jobparameters;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
				.tasklet(helloTasklet(null))
				.build();
	}

	@Bean
	@StepScope
	public Tasklet helloTasklet(
		@Value("#{jobParameters['name']}") String name) {
		return (contribution, chunkContext) -> {
				System.out.printf("Hello %s!\n", name);
				return RepeatStatus.FINISHED;
			};
	}

	public static void main(String[] args) {
		SpringApplication.run(Ch4Ex1Application.class, "name=Son");
	}
}