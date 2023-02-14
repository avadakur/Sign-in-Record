package com.avadakur.sign.service.impl;

import com.avadakur.sign.config.Constants;
import com.avadakur.sign.config.InitDatabase;
import com.avadakur.sign.entity.User;
import com.avadakur.sign.service.SignInService;
import com.avadakur.sign.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;



@Service
public class SignInServiceImpl implements SignInService {

    @Autowired
    public RedisUtil redisUtil;


    @Override
    public int sign(Long userId) {
        if (!checkUser(userId)) {
            return -1;
        }
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonth().getValue();
        int dayOfMonth = now.getDayOfMonth();
        StringBuilder sb = new StringBuilder();
        String date = sb.append(year).append(month).toString();
        String key = String.format(Constants.prefix, userId, date);
        redisUtil.setBit(key, dayOfMonth, true);
        return 1;
    }


    public boolean checkUser(Long userId) {
        User user = InitDatabase.database.get(userId);
        if (null == user) {
            return false;
        }
        return true;
    }
}
