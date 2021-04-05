package com.it.shw.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.shw.common.Response;
import com.it.shw.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RestTemplateUtilTests {

    //测试接口的地址
    private final String URL = "http://localhost:9024";

    @Autowired
    private RestTemplate restTemplate;

    private ObjectMapper mapper;

    private User user;

    private Map<String, String> headParams;

    private final static String TOKEN = "USER_TOKEN";

    private final static String RETURN_USER_NAME = "token_USER_TOKEN";

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper();
        user = new User(1L, "thailandking");
        headParams = new HashMap<>();
        headParams.put("token", TOKEN);
    }

    @Test
    public void postJsonData() throws JsonProcessingException {
        Response response = RestTemplateUtil.postJson(restTemplate, URL + "/post/json", mapper.writeValueAsString(user), headParams);
        Assertions.assertEquals(response.getCode(), "ok");
        Assertions.assertEquals(response.getData(), RETURN_USER_NAME);
    }

    @Test
    public void deletePathData() {
        Object[] params = new Object[]{1};
        Response response = RestTemplateUtil.deletePath(restTemplate, URL + "/delete/path", params, headParams);
        Assertions.assertEquals(response.getCode(), "ok");
        Assertions.assertEquals(response.getData(), RETURN_USER_NAME);
    }

    @Test
    public void putJsonData() throws JsonProcessingException {
        Response response = RestTemplateUtil.putJson(restTemplate, URL + "/put/json", mapper.writeValueAsString(user), headParams);
        Assertions.assertEquals(response.getCode(), "ok");
        Assertions.assertEquals(response.getData(), RETURN_USER_NAME);
    }

    @Test
    public void getPathData() {
        Object[] params = new Object[]{1};
        Response response = RestTemplateUtil.getPath(restTemplate, URL + "/get/path", params, headParams);
        Assertions.assertEquals(response.getCode(), "ok");
        Assertions.assertEquals(response.getData(), RETURN_USER_NAME);
    }

    @Test
    public void getParamsData() {
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("id", "1");
        inputParams.put("name", "thailandking");
        Response response = RestTemplateUtil.getParams(restTemplate, URL + "/get/params", inputParams, headParams);
        Assertions.assertEquals(response.getCode(), "ok");
        Assertions.assertEquals(response.getData(), RETURN_USER_NAME);
    }
}