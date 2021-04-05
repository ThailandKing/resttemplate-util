package com.it.shw.utils;

import com.it.shw.common.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Copyright: Harbin Institute of Technology.All rights reserved.
 * @Description: RestTemplate工具类
 * @author: thailandking
 * @since: 2020/3/19 15:24
 * @history: 1.2020/3/19 created by thailandking
 */
public class RestTemplateUtil {

    // 私有构造
    private RestTemplateUtil() {
    }

    /**
     * @Author thailandking
     * @Date 2020/3/19 17:37
     * @LastEditors thailandking
     * @LastEditTime 2020/3/19 17:37
     * @Description 获取自定义header
     */
    private static HttpHeaders getCustomHeaders(Map<String, String> headerParams, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(headerParams);
        if (mediaType != null) {
            headers.setContentType(mediaType);
        }
        return headers;
    }

    /**
     * @Author thailandking
     * @Date 2020/3/19 17:47
     * @LastEditors thailandking
     * @LastEditTime 2020/3/19 17:47
     * @Description 处理调用结果
     */
    private static Response handleResult(ResponseEntity<String> resp) {
        if (resp.getStatusCodeValue() == 200) {
            String body = resp.getBody();
            return Response.ok().data(body);
        }
        return Response.error("调用异常");
    }

    /**
     * All 请求Path数据
     *
     * @param customRestTemplate 调用RestTemplate
     * @param method             请求method
     * @param url                请求URL
     * @param pathParams         请求Path数据
     * @param headerParams       头部参数
     * @return Response          统一响应对象
     */
    private static Response allPath(RestTemplate customRestTemplate, HttpMethod method, String url, Object[] pathParams, Map<String, String> headerParams) {
        HttpHeaders customHeaders = getCustomHeaders(headerParams, null);
        for (Object pathParam : pathParams) {
            url = url + "/" + pathParam;
        }
        HttpEntity entity = new HttpEntity<>(null, customHeaders);
        ResponseEntity<String> resp = customRestTemplate.exchange(url, method, entity, String.class);
        return handleResult(resp);
    }

    /**
     * All 请求Json数据
     *
     * @param customRestTemplate 调用RestTemplate
     * @param method             请求method
     * @param url                请求URL
     * @param jsonParams         请求Json数据
     * @param headerParams       头部参数
     * @return Response          统一响应对象
     */
    private static Response allJson(RestTemplate customRestTemplate, HttpMethod method, String url, String jsonParams, Map<String, String> headerParams) {
        HttpHeaders customHeaders = getCustomHeaders(headerParams, new MediaType("application", "json", StandardCharsets.UTF_8));
        HttpEntity entity = new HttpEntity<>(jsonParams, customHeaders);
        ResponseEntity<String> resp = customRestTemplate.exchange(url, method, entity, String.class);
        return handleResult(resp);
    }

    /**
     * POST 请求Json数据
     *
     * @param customRestTemplate 调用RestTemplate
     * @param url                请求URL
     * @param jsonParams         请求Json数据
     * @param headerParams       头部参数
     * @return Response          统一响应对象
     */
    public static Response postJson(RestTemplate customRestTemplate, String url, String jsonParams, Map<String, String> headerParams) {
        return allJson(customRestTemplate, HttpMethod.POST, url, jsonParams, headerParams);
    }

    /**
     * DELETE 请求Path数据
     *
     * @param customRestTemplate 调用RestTemplate
     * @param url                请求URL
     * @param pathParams         请求Path数据
     * @param headerParams       头部参数
     * @return Response          统一响应对象
     */
    public static Response deletePath(RestTemplate customRestTemplate, String url, Object[] pathParams, Map<String, String> headerParams) {
        return allPath(customRestTemplate, HttpMethod.DELETE, url, pathParams, headerParams);
    }

    /**
     * PUT 请求Json数据
     *
     * @param customRestTemplate 调用RestTemplate
     * @param url                请求URL
     * @param jsonParams         请求Json数据
     * @param headerParams       头部参数
     * @return Response          统一响应对象
     */
    public static Response putJson(RestTemplate customRestTemplate, String url, String jsonParams, Map<String, String> headerParams) {
        return allJson(customRestTemplate, HttpMethod.PUT, url, jsonParams, headerParams);
    }

    /**
     * GET 请求Path数据
     *
     * @param customRestTemplate 调用RestTemplate
     * @param url                请求URL
     * @param pathParams         请求Path数据
     * @param headerParams       头部参数
     * @return Response          统一响应对象
     */
    public static Response getPath(RestTemplate customRestTemplate, String url, Object[] pathParams, Map<String, String> headerParams) {
        return allPath(customRestTemplate, HttpMethod.GET, url, pathParams, headerParams);
    }

    /**
     * GET 请求Params数据
     * inputParams必须为String
     *
     * @param customRestTemplate 调用RestTemplate
     * @param url                请求URL
     * @param inputParams        请求Params数据
     * @param headerParams       头部参数
     * @return Response          统一响应对象
     */
    public static Response getParams(RestTemplate customRestTemplate, String url, Map<String, String> inputParams, Map<String, String> headerParams) {
        HttpHeaders customHeaders = getCustomHeaders(headerParams, null);
        HttpEntity entity = new HttpEntity<>(customHeaders);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.setAll(inputParams);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.queryParams(params).build().encode().toUri();
        ResponseEntity<String> resp = customRestTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        return handleResult(resp);
    }
}
