package com.leyou.auth.controller;

import com.leyou.auth.service.AuthService;
import com.leyou.common.pojo.utils.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;

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


}
