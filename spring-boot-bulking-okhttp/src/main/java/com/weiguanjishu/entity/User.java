package com.weiguanjishu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 微信公众号：微观技术
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {


    private Long id;

    /**
     * 名称
     */
    private String userName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 地址
     */
    private String address;
}

