package by.clevertec.controller;

import by.clevertec.proxy.ProxyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestClientController {
    @Autowired
    ProxyClient proxyClient;
    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public ResponseEntity<String> connect(){
        String answer = proxyClient.getAnswer();

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

}
