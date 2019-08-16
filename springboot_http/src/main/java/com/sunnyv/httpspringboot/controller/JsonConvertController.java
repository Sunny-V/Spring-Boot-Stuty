package com.sunnyv.httpspringboot.controller;

import com.sunnyv.httpspringboot.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author lihao
 * @create 2019-08-15 23:10
 */
@RestController
public class JsonConvertController {
    @GetMapping("/testjson")
    public Object testjson(){

        return new User(111, "abc123", "10001000", new Date());
    }
}
