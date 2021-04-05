package com.it.shw.controller;

import com.it.shw.entity.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Copyright: Harbin Institute of Technology.All rights reserved.
 * @Description: 用于测试提供的服务接口
 * @author: thailandking
 * @since: 2020/3/19 17:06
 * @history: 1.2020/3/19 created by thailandking
 */
@RestController
public class DataController {

    //1、post请求json数据
    @PostMapping(value = "/post/json")
    public String postJsonData(@RequestBody User user, HttpServletRequest request) {
        String token = request.getHeader("token");
        user.setName("token_" + token);
        return user.getName();
    }

    //2、delete请求path数据
    @DeleteMapping(value = "/delete/path/{id}")
    public String deletePathData(@PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("token");
        User user = new User();
        user.setName("token_" + token);
        return user.getName();
    }

    //3、put请求json数据
    @PutMapping(value = "/put/json")
    public String putJsonData(@RequestBody User user, HttpServletRequest request) {
        String token = request.getHeader("token");
        user.setName("token_" + token);
        return user.getName();
    }

    //4、get请求path数据
    @GetMapping(value = "/get/path/{id}")
    public String getPathData(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        String token = request.getHeader("token");
        User user = new User();
        user.setName("token_" + token);
        return user.getName();
    }

    //5、get请求params数据
    @GetMapping(value = "/get/params")
    public String getParamsData(User user, HttpServletRequest request) {
        String token = request.getHeader("token");
        user.setName("token_" + token);
        return user.getName();
    }
}