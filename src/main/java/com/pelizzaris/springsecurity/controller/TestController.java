package com.pelizzaris.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@Controller
public class TestController {

    @RequestMapping(value = "/hello")
    public String helloWorld(){
        return "Hello, World!";
    }
}
