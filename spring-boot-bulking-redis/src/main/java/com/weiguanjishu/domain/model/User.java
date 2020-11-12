package com.weiguanjishu.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 微信公众号：微观技术
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

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

