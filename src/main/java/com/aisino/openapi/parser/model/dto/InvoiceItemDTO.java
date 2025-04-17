package com.aisino.openapi.parser.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDTO {
    private String name;             // 项目名称
    private String specification;    // 规格型号
    private String unit;             // 单位
    private BigDecimal quantity;     // 数量
    private BigDecimal unitPrice;    // 单价
    private BigDecimal amount;       // 金额
    private String taxRate;          // 税率/征收率
    private BigDecimal tax;          // 税额
}