package com.leyou.user.controller;

import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("code")
    public ResponseEntity<Void> verifyCode(@RequestParam("phone")String phone){
       this.userService.verifyCode(phone);
       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
