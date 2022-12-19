package com.example.springproject.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ComponentScan
@RestController()
@RequestMapping("/Hello")
public class helloTest {
    @CrossOrigin
    @RequestMapping("/hello")
    public String hello(){
        System.out.println("hello");
        return "Hello world";
    }

}
