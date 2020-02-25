package com.leyou.user.service;

import com.leyou.common.pojo.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final static String key_prefix="user:verify:";


    /**
     * 校验用户名和手机号是否可用
     * @param data
     * @param type
     * @return
     */
    public Boolean checkUser(String data, Integer type) {
        User record = new User();
        if (type==1){
            record.setUsername(data);
        } else if (type==2) {
            record.setPhone(data);
        }else {
            return null;
        }
       return this.userMapper.selectCount(record)==0;
    }


    public void verifyCode(String phone) {
        //判断phone是否为空
        if (StringUtils.isBlank(phone)){
            return;
        }
        //生成验证码
        String code = NumberUtils.generateCode(6);
        //发送验证码（发送给消息给队列）
        Map<String,String> msg= new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        this.amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE","sms.verify",msg);
        //缓存验证码
        this.stringRedisTemplate.opsForValue().set(key_prefix+phone,code,5, TimeUnit.MINUTES);

    }
}
