package com.avadakur.sign.service.impl;

import com.avadakur.sign.config.Constants;
import com.avadakur.sign.service.QueryService;
import com.avadakur.sign.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    public RedisUtil redisUtil;

    @Override
    public HashMap<Integer, Integer> getRecordByMonth(Long userId) {
        HashMap<Integer, Integer> result = new HashMap<>();
        String key = genKey(userId);
        int dayOfMonth = getDayOfMonth();
        List<Long> bitField = redisUtil.bitField(key, dayOfMonth, 1);
        if (bitField == null || CollectionUtils.isEmpty(bitField)) {
            return result;
        }
        Long bit = bitField.get(0);
        if (bit == null || bit == 0) {
            return result;
        }

        for (int i = dayOfMonth; i >= 1; i--) {
            if ((bit & 1) == 1) {
                result.put(i, 1);
            }else {
                result.put(i, 0);
            }
            bit >>>= 1;
        }

        return result;
    }

    @Override
    public int getDaysSignIn(Long userId) {
        String key = genKey(userId);
        int dayOfMonth = getDayOfMonth();
        List<Long> bitField = redisUtil.bitField(key, dayOfMonth, 1);

        int count = 0;
        if (bitField == null || CollectionUtils.isEmpty(bitField)) {
            return 0;
        }
        Long bit = bitField.get(0);
        if (bit == null || bit == 0) {
            return 0;
        }

        boolean flag = true;

        while (true) {
            if ((bit & 1) == 1) {
                count++;
                flag = false;
            }
            if (!flag && (bit & 1) == 0) {
                break;
            }
            bit >>>= 1;
        }

        return count;
    }

    @Override
    public boolean checkSignRecord(Long userId, int day) {
        String key = genKey(userId);
        return redisUtil.getBit(key, day);
    }

    public String genKey(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonth().getValue();
        int year = now.getYear();
        StringBuilder sb = new StringBuilder();
        String date = sb.append(year).append(month).toString();
        return String.format(Constants.prefix, userId, date);
    }

    public int getDayOfMonth() {
        return LocalDateTime.now().getDayOfMonth();
    }
}
