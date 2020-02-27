package com.leyou.auth.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.pojo.utils.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public ResponseEntity<String> Login(@RequestParam("username")String username, @RequestParam("password")String password,
                                        HttpServletRequest request, HttpServletResponse response){
       //调用service方法生成jwt
     String token =  this.authService.login(username,password);
     if (StringUtils.isBlank(token)){
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
     }
                //setcookie方法，把jwt类型的token设置给浏览器cookie
        CookieUtils.setCookie(request,response,"LY_TOKEN",token,60*30,"utf-8",true);
        String s = "cookie设置成功";
        return ResponseEntity.ok(s);
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN")String token,HttpServletResponse response,HttpServletRequest request) throws Exception {
        UserInfo userInfo = this.authService.verift(token);
        //若为空就是为正常登录
        if (userInfo==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //为了保持人在，就保持登录。需要每次获取userinfo都要刷新过期时间
        //1.刷信jwt过期时间==本质：重新生成jwt
       token = JwtUtils.generateToken(userInfo,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());
        //2.刷信cookie过期时间
        CookieUtils.setCookie(request,response,"LY_TOKEN",token,60*30,"utf-8",true);
        return ResponseEntity.ok(userInfo);
    }


}
