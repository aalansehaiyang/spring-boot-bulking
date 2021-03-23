package com.weiguanjishu.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Storage {
    private Integer id;
    private String commodityCode;
    private Integer count;
}
