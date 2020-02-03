package cn.itcast.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@EnableDiscoveryClient//开启erueka的客户端，这注解由spring提供，封装了奈飞的erueka客户端@EnableEurekaClient
@SpringBootApplication
@MapperScan("cn.itcast.service.mapper")//开启mapper接口扫描。不需要再mapper上加@mapper注解
public class ItcastServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItcastServiceProviderApplication.class, args);
    }

}
