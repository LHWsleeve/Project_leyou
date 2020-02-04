package cn.itcast.service.client;

import cn.itcast.service.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-provider")
public interface UserClient {

    @GetMapping("/user/{id}")
    public User querById(@PathVariable("id") Integer id);


}