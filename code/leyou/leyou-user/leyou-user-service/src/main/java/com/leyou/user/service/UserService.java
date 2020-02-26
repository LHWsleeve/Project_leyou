package com.leyou.user.service;

import com.leyou.common.pojo.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        this.stringRedisTemplate.opsForValue().set(key_prefix+phone,code,5, TimeUnit.HOURS);

    }

    public void register(User user, String code) {
       // 1.校验验证码
      String s= key_prefix + user.getPhone();
        String redisCode = this.stringRedisTemplate.opsForValue().get(key_prefix + user.getPhone());
        if (!StringUtils.equals(redisCode,code)){
            return;
        }
        //   2.生成随机码
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //3.加盐加密存储 MD5
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        // 4.新增用户信息
        user.setId(null);
        user.setCreated(new Date());
        this.userMapper.insertSelective(user);
        //验证成功之后删除验证码
        this.stringRedisTemplate.delete(key_prefix+user.getPhone());
    }

    public User queryUser(String username, String password) {
        //根据用户名查询用户
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        //对用户输入的密码加盐加密
        if (user==null){
            return null;
        }
        password = CodecUtils.md5Hex(password, user.getSalt());
        //判断用户输入的密码是否正确
        if (!StringUtils.equals(password,user.getPassword())){
            return null;
        }
        return user;
    }
}
