package by.clevertec.service.interfaces;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class NewsServiceTest {

    @Autowired
    NewsService newsService;

    @Test
    public void getAll(){
        System.out.println(newsService.findAll());
    }

}