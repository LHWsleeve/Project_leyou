package cn.itcast.springboot;

import cn.itcast.springboot.controller.HelloController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@EnableAutoConfiguration //启用自动配置
//@ComponentScan //等于原先的配置文件中包扫描机制.默认扫描当前所在类下的所有包和子包
@SpringBootApplication//等效于上面俩
public class TestApplication {//此时这个类被称为引导类，是springboot应用程序的入口

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class,args);
    }
}
