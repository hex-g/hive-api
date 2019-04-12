package hive.album;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EntityScan( basePackages = {"hive.entity.user"} )
public class SmugshotApplication {

  public static void main(String[] args) {
    SpringApplication.run(SmugshotApplication.class, args);
  }
}
