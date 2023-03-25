package by.clevertec;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ApplicationRunner {
    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(ApplicationRunner.class);
        springApplication.setAdditionalProfiles("dev");
        springApplication.run(args);
    }
}
