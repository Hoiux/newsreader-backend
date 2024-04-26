package org.hoiux.newsreader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class NewsReader {

	public static void main(String[] args) {
		SpringApplication.run(NewsReader.class, args);
	}

}
