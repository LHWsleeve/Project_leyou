package cn.itcast.user.controller;

import cn.itcast.user.pojo.User;
import cn.itcast.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "hello test";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User queryById(@PathVariable("id") Integer id){
        return this.userService.querUserById(id);
    }

    @GetMapping("all")
    public String queryUserAll(Model model){
        List<User> userList = this.userService.queryUserAll();
        model.addAttribute("users",userList);
        return "users";
    }
}
