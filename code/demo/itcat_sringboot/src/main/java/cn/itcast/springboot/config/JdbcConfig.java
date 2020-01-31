package cn.itcast.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration//声明一个类是一个java配置类，相当于XML配置
@PropertySource("classpath:jdbc.properties")//读取外部资源文件
public class JdbcConfig {
    @Value("${jdbc.driverClassName}")//获取spring容器中的资源文件配置
    private String driverClassName;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String  username;
    @Value("${jdbc.password}")
    private String  password;

    @Bean//把方法的返回值注入spring容器
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(this.driverClassName);
        dataSource.setUrl(this.url);
        dataSource.setUrl(this.username);
        dataSource.setUrl(this.password);
        return dataSource;
    }
}
