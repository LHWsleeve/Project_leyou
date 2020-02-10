package com.leyou.upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class LyouCorsFliterConfig {

    @Bean
    public CorsFilter corsFilter(){
        //初始化配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许跨域的域名，可以指设置多个。*：表示所有域名，如果携带cookie一定不能写*
        corsConfiguration.addAllowedOrigin("http://manage.leyou.com");
//        corsConfiguration.addAllowedOrigin("http://api.leyou.com");
//        corsConfiguration.addAllowedOrigin("http://www.leyou.com");
        //允许写代cookie
        corsConfiguration.setAllowCredentials(true);
        //允许所有i请求方式跨域访问
        corsConfiguration.addAllowedMethod("*");
        //允许携带所有头信息跨域访问
        corsConfiguration.addAllowedHeader("*");
        //初始化配置源
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        //拦截所有请求，校验是否允许跨域
        configurationSource.registerCorsConfiguration("/**",corsConfiguration);
        //返回过滤器对象
        return new CorsFilter(configurationSource);
    }
}
