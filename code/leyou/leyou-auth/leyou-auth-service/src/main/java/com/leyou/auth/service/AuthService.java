package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserClient userClient;

    public String login(String username, String password) {
        //远程调用用户中心的查询接口
        User user = this.userClient.queryUser(username, password);
        //判断用户是否为空
        if (user==null){
            return null;
        }
        //初始化载荷userinfo
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setId(user.getId());
        //若不为空，生成jwt类型token
        try {
           return JwtUtils.generateToken(userInfo,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserInfo verift(String token) {
        try {
            //使用公钥解析jwt获取用户信息
            return  JwtUtils.getInfoFromToken(token,this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
