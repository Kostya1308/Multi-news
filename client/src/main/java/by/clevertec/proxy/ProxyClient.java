package by.clevertec.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "news", url = "localhost:8080")
public interface ProxyClient {
@GetMapping(value = "/news/1")
    public String getAnswer();
}
