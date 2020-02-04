package cn.itcast.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
//@EnableDiscoveryClient
//@SpringBootApplication
//@EnableCircuitBreaker//开启熔断组件
@SpringCloudApplication//这个相当于上面三个注解之和
@EnableFeignClients//开启feign组件
public class ItcastServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItcastServiceConsumerApplication.class, args);
    }

    @LoadBalanced//开启ribbon的负载均衡
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
