package by.clevertec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class SpringBootClientApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SpringBootClientApplication.class);
        springApplication.run(args);
    }
}