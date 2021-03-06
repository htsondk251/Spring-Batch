package com.example.ch4ex2;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class Ch4Ex2Application {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job job() {
		return this.jobBuilderFactory
			.get("helloSomebody")
			// .incrementer(new RunIdIncrementer())
			.start(step1())
			.build();

	}

	@Bean
	public Step step1() {
		return this.stepBuilderFactory
			.get("step1")
			.tasklet(tasklet())
			.build();
	}

	@Bean
	public Tasklet tasklet() {
		return (contribution, chunkContext) -> {
			String name = (String) chunkContext.getStepContext()
										.getJobParameters()
										.get("name");
			System.out.printf("Hello %s!\n", name);
			return RepeatStatus.FINISHED;
		};
	}
	public static void main(String[] args) {
		SpringApplication.run(Ch4Ex2Application.class, args);
	}
}