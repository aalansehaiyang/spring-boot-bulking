package com.weiguanjishu.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 微信公众号：微观技术
 */

@Data
public class QueryUserParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
}
