package com.weiguanjishu.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 微信公众号：微观技术
 */

@Data
@Builder
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @JSONField(ordinal = 1)
    private Long id;

    /**
     * 名称
     */
    @JSONField(ordinal = 2)
    private String userName;

    /**
     * 年龄
     */
    @JSONField(ordinal = 3)
    private Integer age;

    /**
     * 地址
     */
    @JSONField(ordinal = 4)
    private String address;
}

