package com.amigoservers.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @RequestMapping(path = "/helloworld")
    public String helloWorld() {
        return "Hello world!";
    }
}
