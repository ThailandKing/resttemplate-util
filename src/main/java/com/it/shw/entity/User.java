package com.it.shw.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Copyright: Harbin Institute of Technology.All rights reserved.
 * @Description: 用户实体类
 * @author: thailandking
 * @since: 2020/3/19 17:00
 * @history: 1.2020/3/19 created by thailandking
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
}
