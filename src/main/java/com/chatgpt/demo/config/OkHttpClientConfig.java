package com.chatgpt.demo.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * okhttp config
 */
@Configuration
public class OkHttpClientConfig {

    @Value("${token}")
    private String token;

    @Value("${okhttp3.OkHttpClient.connectTimeout}")
    private int okHttpClientConnectTimeout;
    @Value("${okhttp3.OkHttpClient.readTimeout}")
    private int okHttpClientReadTimeout;
    @Value("${okhttp3.OkHttpClient.writeTimeout}")
    private int okHttpClientWriteTimeout;
    @Value("${okhttp3.ConnectionPool.maxIdleConnections}")
    private int okHttpClientMaxIdleConnections;
    @Value("${okhttp3.ConnectionPool.keepAliveDuration}")
    private int okHttpClientKeepAliveDuration;


    @Bean
    public OkHttpClient okHttpClient() {
        ConnectionPool connectionPool = new ConnectionPool(okHttpClientMaxIdleConnections, okHttpClientKeepAliveDuration, TimeUnit.SECONDS);

        // todo 代理IP设置
        // Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHostname, proxyPort));
        Proxy proxy = Proxy.NO_PROXY;

        return new OkHttpClient.Builder()
                .proxy(proxy)
                .connectionPool(connectionPool)
                .connectTimeout(okHttpClientConnectTimeout, TimeUnit.SECONDS)
                .readTimeout(okHttpClientReadTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpClientWriteTimeout, TimeUnit.SECONDS)
                .addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                        .addHeader("Authorization", StringUtils.join("Bearer ", token))
                        .build()))
                .build();
    }

}
