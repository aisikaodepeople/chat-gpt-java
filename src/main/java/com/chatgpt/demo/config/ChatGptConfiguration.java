package com.chatgpt.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class ChatGptConfiguration {

    @Bean("threadPool")
    public Executor threadPool() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(16);
        threadPoolTaskExecutor.setMaxPoolSize(32);
        threadPoolTaskExecutor.setQueueCapacity(200);
        threadPoolTaskExecutor.setKeepAliveSeconds(5 * 60);
        threadPoolTaskExecutor.setThreadNamePrefix("threadPool-");
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    /**
     * 解决跨域问题
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.setMaxAge(10000L);
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(corsConfigurationSource);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
