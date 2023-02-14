package com.avadakur.sign.controller;


import com.avadakur.sign.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @Autowired
    public RedisUtil redisUtil;


    @RequestMapping("/test")
    public String test() {
        redisUtil.set("test", "hello,world");
        return redisUtil.get("test");
    }
}
