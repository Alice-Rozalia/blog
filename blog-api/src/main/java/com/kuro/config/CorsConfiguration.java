package com.kuro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfiguration {
    @Bean
    public CorsFilter corsFilter() {
        // 初始化cors配置对象
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        // 允许跨域的域名，如果要携带cookie则不能写*。
        configuration.addAllowedOrigin("*");
        // 是否允许携带cookie
        configuration.setAllowCredentials(true);
        // * 代表所有的请求方法
        configuration.addAllowedMethod("*");
        // 允许携带任何头信息
        configuration.addAllowedHeader("*");

        // 初始化cors配置源对象
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", configuration);

        // 返回CorsFilter实例，需要一个参数: cors配置源对象
        return new CorsFilter(configurationSource);
    }
}
