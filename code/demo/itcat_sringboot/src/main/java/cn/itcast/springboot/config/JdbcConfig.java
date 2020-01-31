package cn.itcast.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration//声明一个类是一个java配置类，相当于XML配置
//@PropertySource("classpath:jdbc.properties")//读取外部资源文件
@EnableConfigurationProperties(JdbcProperties.class)//起用资源配置读取类
public class JdbcConfig {
    @Autowired
    private JdbcProperties jdbcProperties;

    @Bean//把方法的返回值注入spring容器
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(this.jdbcProperties.getDriverClassName());
        dataSource.setUrl(this.jdbcProperties.getUrl());
        dataSource.setUrl(this.jdbcProperties.getUsername());
        dataSource.setUrl(this.jdbcProperties.getPassword());
        return dataSource;
    }
}
