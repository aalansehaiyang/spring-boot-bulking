package com.weiguanjishu.entity;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Integer id;
    private String userId;
    private BigDecimal money;
}
