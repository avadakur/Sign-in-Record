package com.avadakur.sign.service;

import java.util.HashMap;

public interface QueryService {

    /**
     * 查询某位用户当月签到情况
     */
    HashMap<Integer, Integer> getRecordByMonth(Long userId);

    /**
     * 查询某位用户最近连续签到天数
     * 如   0 1 1 1 0 1 1 0
     *  最近的连续登录天数为两天
     */
    int getDaysSignIn(Long userId);

    /**
     * 查询某位用户某天是否签到
     */
    boolean checkSignRecord(Long userId, int day);
}
