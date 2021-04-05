package com.it.shw.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    /**
     * Http连接管理器配置
     *
     * @return
     */
    @Bean
    public HttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // 最大连接数
        connectionManager.setMaxTotal(500);
        // 同路由并发数（每个主机的并发）
        connectionManager.setDefaultMaxPerRoute(100);
        return connectionManager;
    }

    /**
     * HttpClient配置
     *
     * @param connectionManager
     * @return
     */
    @Bean
    public HttpClient httpClient(HttpClientConnectionManager connectionManager) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 设置http连接管理器
        httpClientBuilder.setConnectionManager(connectionManager);
        // 设置重试次数
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
        return httpClientBuilder.build();
    }

    /**
     * 请求连接配置
     *
     * @param httpClient
     * @return
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        // 连接池获取请求连接的超时时间，不宜过长，必须设置/毫秒（超时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool）
        clientHttpRequestFactory.setConnectionRequestTimeout(10 * 1000);
        // 连接超时时间/毫秒（连接上服务器(握手成功)的时间，超时抛出connect timeout）
        clientHttpRequestFactory.setConnectTimeout(5 * 1000);
        // 数据读取超时时间(socketTimeout)/毫秒（服务器返回数据(response)的时间，超时抛出read timeout）
        clientHttpRequestFactory.setReadTimeout(10 * 1000);
        return clientHttpRequestFactory;
    }

    /**
     * RestTemplate模板
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        // 配置请求工厂
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        return restTemplate;
    }
}