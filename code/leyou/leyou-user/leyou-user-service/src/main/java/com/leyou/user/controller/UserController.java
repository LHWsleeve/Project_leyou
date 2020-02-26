package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.bouncycastle.voms.VOMSAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 校验用户名或手机号是否可以
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data")String data ,@PathVariable("type") Integer type)
    {
        Boolean b = this.userService.checkUser(data,type);
        if (b==null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(b);
    }

    @PostMapping("/code")
    public ResponseEntity<String> verifyCode(@RequestParam("phone")String phone){
        this.userService.verifyCode(phone);
         String s ="发送成功";
        return ResponseEntity.ok(s);
    }

    @PostMapping("register")
    public ResponseEntity<String> regidter(@Valid User user, @RequestParam("code")String codde){
        this.userService.register(user,codde);
        String s = "register发送成功";
        return ResponseEntity.ok(s);
    }

    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username")String username,@RequestParam("password")String password){
        User user =this.userService.queryUser(username,password);
        if (user==null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }
}
