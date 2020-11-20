package com.spring.springintegrationexample;

import com.spring.springintegrationexample.integration.gateway.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@SpringBootApplication
public class SpringIntegrationExampleApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationExampleApplication.class, args);
	}

	@Autowired
	private Sender sender;

	@Override
	public void run(String... args) throws Exception {
		File inputFile = new File("rss-feed.xml");
		InputStream inputStream = new FileInputStream(inputFile);
		sender.send(inputStream);
	}
}
