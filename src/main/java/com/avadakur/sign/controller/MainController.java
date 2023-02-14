package com.avadakur.sign.controller;


import com.alibaba.fastjson.JSON;
import com.avadakur.sign.service.QueryService;
import com.avadakur.sign.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主要的路由和方法
 */
@RestController
@RequestMapping("/user")
public class MainController {


    @Autowired
    public SignInService signInService;

    @Autowired
    public QueryService queryService;

    @RequestMapping("/sign")
    public int sign(@RequestParam("userId") Long userId) {
        return signInService.sign(userId);
    }

    @RequestMapping("/queryRecord")
    public String queryRecord(@RequestParam("userId") Long userId) {
        return JSON.toJSONString(queryService.getRecordByMonth(userId));
    }

    @RequestMapping("/queryDaysSignIn")
    public int queryDaysSignIn(@RequestParam("userId") Long userId) {
        return queryService.getDaysSignIn(userId);
    }

    @RequestMapping("/checkSignRecord")
    public boolean checkSignRecord(@RequestParam("userId") Long userId, @RequestParam("day") int day) {
        return queryService.checkSignRecord(userId, day);
    }
}
