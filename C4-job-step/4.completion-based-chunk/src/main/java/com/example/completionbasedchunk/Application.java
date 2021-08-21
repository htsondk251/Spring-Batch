package com.example.completionbasedchunk;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class Application {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job job() {
		return this.jobBuilderFactory
			.get("chunkBasedJob")
			.incrementer(new RunIdIncrementer())
			.start(chunkStep())
			.build();

	}

	@Bean
	public Step chunkStep() {
		return this.stepBuilderFactory
					.get("chunkStep")
					.<String, String>chunk(completionPolicy())
					.reader(itemReader())
					.writer(itemWriter())
					.build();
	}

	@Bean
	public CompletionPolicy completionPolicy() {
		CompositeCompletionPolicy policy = new CompositeCompletionPolicy();
		policy.setPolicies(new CompletionPolicy[]{
			new TimeoutTerminationPolicy(3),
			new SimpleCompletionPolicy(1000)
		});
		return policy;
	}
	
	@Bean
	public ListItemReader<String> itemReader() {
		List<String> items = new ArrayList<>(2001);
		for (int i = 0; i < 1001; i++) {
			items.add(UUID.randomUUID().toString());
		}
		return new ListItemReader<>(items);
	}

	@Bean
	public ItemWriter<String> itemWriter() {
		return items -> {
			int count = 0;
			for (String item : items) {
				System.out.println(">> current item = " + item + "-" + count++);
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}
}