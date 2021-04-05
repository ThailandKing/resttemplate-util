## 一、常见Restful接口

### 1、POST

- json

```java
//1、post请求json数据
@PostMapping(value = "/post/json")
public R postJsonData(@RequestBody T t) {
    
}
```

### 2、DELETE

- path

```java
//2、delete请求path数据
@DeleteMapping(value = "/delete/path/{id}")
public R deletePathData(@PathVariable Long id) {
   
}
```

### 3、PUT

- json

```java
//3、put请求json数据
@PutMapping(value = "/put/json")
public R putJsonData(@RequestBody T t) {
    
}
```

### 4、GET

- path

```java
//4、get请求path数据
@GetMapping(value = "/get/path/{id}")
public R getPathData(@PathVariable(value = "id") Long id) {
    
}
```

- params

```java
//5、get请求params数据
@GetMapping(value = "/get/params")
public R getParamsData(T t) {
    
}
```

## 二、基于RestTemplate封装API

### 1、POST 请求Json数据

```java
/**
     * POST 请求Json数据
     *
     * @param customRestTemplate 调用RestTemplate
     * @param url                请求URL
     * @param jsonParams         请求Json数据
     * @param headerParams       头部参数
     * @return Response          统一响应对象
     */
public static Response postJson(RestTemplate customRestTemplate, 
                                String url, 
                                String jsonParams, 
                                Map<String, String> headerParams) {
}
```

### 2、DELETE 请求Path数据

```java
/**
     * DELETE 请求Path数据
     *
     * @param customRestTemplate 调用RestTemplate
     * @param url                请求URL
     * @param pathParams         请求Path数据
     * @param headerParams       头部参数
     * @return Response          统一响应对象
     */
public static Response deletePath(RestTemplate customRestTemplate, 
                                  String url, 
                                  Object[] pathParams, 
                                  Map<String, String> headerParams) {

}
```

### 3、PUT请求Json数据

```java
/**
     * PUT 请求Json数据
     *
     * @param customRestTemplate 调用RestTemplate
     * @param url                请求URL
     * @param jsonParams         请求Json数据
     * @param headerParams       头部参数
     * @return Response          统一响应对象
     */
public static Response putJson(RestTemplate customRestTemplate, 
                               String url, 
                               String jsonParams, 
                               Map<String, String> headerParams) {
    
}
```

### 4、GET 请求Path数据

```java
/**
     * GET 请求Path数据
     *
     * @param customRestTemplate 调用RestTemplate
     * @param url                请求URL
     * @param pathParams         请求Path数据
     * @param headerParams       头部参数
     * @return Response          统一响应对象
     */
public static Response getPath(RestTemplate customRestTemplate, 
                               String url, 
                               Object[] pathParams, 
                               Map<String, String> headerParams) {
    
}
```

### 5、GET 请求Params数据

```java
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
public static Response getParams(RestTemplate customRestTemplate, 
                                 String url, 
                                 Map<String, String> inputParams, 
                                 Map<String, String> headerParams) {
    
}
```

## 三、基于SpringBoot项目使用

### 1、添加依赖

```xml
<!--httpclient-->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.13</version>
</dependency>
```

### 2、书写RestTemplate配置

```java
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
```

### 3、定义统一响应对象

```java
@Data
public class Response {

    private String code;

    private String message;

    private Object data;

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Response error(String message) {
        return new Response("error", message);
    }

    public static Response ok() {
        return new Response("ok", null);
    }

    public Response data(Object data) {
        this.data = data;
        return this;
    }
}
```

### 4、导入工具类

```java
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
```

### 5、测试调用

```java
@Test
public void postJsonData() throws JsonProcessingException {
    Response response = RestTemplateUtil.postJson(restTemplate, URL + "/post/json", mapper.writeValueAsString(user), headParams);
    Assertions.assertEquals(response.getCode(), "ok");
    Assertions.assertEquals(response.getData(), RETURN_USER_NAME);
}
```

## 源码链接

- https://github.com/ThailandKing/resttemplate-util.git

