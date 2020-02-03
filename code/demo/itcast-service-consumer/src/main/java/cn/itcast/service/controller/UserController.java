package cn.itcast.service.controller;

import cn.itcast.service.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/consumer/user")
@RestController
public class UserController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public User querUserById(){
       return this.restTemplate.getForObject("http://localhost:10001/user/1",User.class);
     }
}
