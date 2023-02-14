package com.avadakur.sign.config;


import com.avadakur.sign.entity.User;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;


@Configuration
public class InitDatabase {


    public static HashMap<Long, User> database = new HashMap<Long, User>();

    @PostConstruct
    public void init() {
        for (int i = 1; i <= 5; i++) {
            Long longId = (long) i;
            database.put(longId, new User(longId, "user" + i));
        }
    }

}
