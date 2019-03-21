package hive.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RepositoryApplication {
  public static void main(String[] args) {
    SpringApplication.run(RepositoryApplication.class, args);
  }
}
