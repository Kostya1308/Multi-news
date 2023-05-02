package by.clevertec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationRunner {
    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(ApplicationRunner.class);
        springApplication.setAdditionalProfiles("dev");
        springApplication.run(args);
    }
}
