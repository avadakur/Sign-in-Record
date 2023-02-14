# Sign-in-Record
##### *Aavadakur*
### A Micro Service About How to Record Huge Number of People Sign in and Count.
### 一个微服务用来统计在大量用户情况下节省空间进行签到统计的项目
<br />

* This project will contain some popular tools.
* And it just a simple demo to help some developers to make sign in business.
* There is the tools the project will use.
* 这个项目包含一些比较流行的框架和工具
* 它只是一个简单的演示，帮助一些开发人员学习如何进行签到统计业务。
* 这是项目将使用的工具。


##### The main business is base on Redis, some feature about bitmap structure.
##### 主要业务是基于Redis，位图结构的一些特性。
<br />
<br />

##### The main principle will be show you.

1. **Springboot**
2. **Redis**
3. **SpringMvc**
4. **Postman**

### Main method

1. First of all, you know that bitmap is a data structure can storage many 0 and 1 in one key.
2. If storage all the sign-in record in mysql, it will be so huge, and waste a lot of space.
3. So we use bitmap to help us storage data.
4. Some bitmap api.
> http://t.csdn.cn/qUIy7

**核心代码解读**

##### 登录签到
```java
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
        //组成对应的key 
        //USER:SIGN:ID:1:DATE:20232
        //dayOfMonth为当天也是在对应bitmap key中的offset，今天是14日，所以偏移为14
        //true为签到，bitmap中为1
        redisUtil.setBit(key, dayOfMonth, true);
        return 1;
    }
```

##### 查询某位用户当月签到情况
```java
    @Override
    public HashMap<Integer, Integer> getRecordByMonth(Long userId) {
        HashMap<Integer, Integer> result = new HashMap<>();
        //生成key和当前时间
        String key = genKey(userId);
        int dayOfMonth = getDayOfMonth();
        //bitfield方法为获取对应offset到dayOfMonth中间二进制值，返回一个十进制数
        // 今天为4日， 1日没有签到，2、3、4日签到，对应方法为
        // redisUtil.bitField(key, 4, 1)  返回值为十进制7 实际为二进制0111
        List<Long> bitField = redisUtil.bitField(key, dayOfMonth, 1);
        if (bitField == null || CollectionUtils.isEmpty(bitField)) {
            return result;
        }
        Long bit = bitField.get(0);
        if (bit == null || bit == 0) {
            return result;
        }
        //利用java的与运算将bitfield与1进行与运算，即可得到对应日期的签到情况
        for (int i = dayOfMonth; i >= 1; i--) {
            if ((bit & 1) == 1) {
                result.put(i, 1);
            }else {
                result.put(i, 0);
            }
            bit >>>= 1;
        }
        // {1:0,2:0,3:0,4:0,5:0,6:1,7:1,8:1,9:1,10:1,11:0,12:1,13:1,14:1}
        return result;
    }
```

### By the way, I am a Teleworker,I can pick some project about Java and Vue
### email: 1432607025@qq.com