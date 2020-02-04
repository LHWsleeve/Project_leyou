package cn.itcast.service.controller;

import cn.itcast.service.pojo.User;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequestMapping("/consumer/user")
@RestController
public class UserController {
    @Autowired
    private RestTemplate restTemplate;
//    @Autowired
//    private DiscoveryClient discoveryClient;//服务地址列表。是Y哦那个ribbon时不需要

    @GetMapping
    public User querUserById(@RequestParam("id")Integer id){
        //通过服务的id获取服务的集合（实际工作中可能是集群，所以返回集合）
//        List<ServiceInstance> instances = discoveryClient.getInstances("SERVICE-PROVIDER");
//        ServiceInstance serviceInstance = instances.get(0);
//        return this.restTemplate.getForObject("http://"+serviceInstance.getHost()+":"+serviceInstance.getPort()+"/user/"+id,User.class);
        //由ribbon决定调用那个服务器（地址和端口），通过服务名称获取列表，不能使用固定端口。

        return this.restTemplate.getForObject("http://service-provider/user/"+id,User.class);
    }

}
