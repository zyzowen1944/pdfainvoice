package com.aisino.openapi.parser.model.dto;


import com.aisino.openapi.parser.model.enums.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private String invoiceNumber;        // 发票号码
    private InvoiceType invoiceType;     // 发票类型
    private LocalDate issueDate;         // 开票日期

    // 购买方信息
    private String buyerName;            // 购买方名称
    private String buyerTaxId;           // 购买方税号

    // 销售方信息
    private String sellerName;           // 销售方名称
    private String sellerTaxId;          // 销售方税号

    // 金额信息
    private BigDecimal totalAmount;      // 合计金额
    private BigDecimal totalTax;         // 合计税额
    private BigDecimal totalWithTax;     // 价税合计
    private String amountInWords;        // 大写金额

    // 明细信息
    private List<InvoiceItemDTO> items;  // 发票明细

    // 其他信息
    private String remark;               // 备注
    private String additionalInfo;       // 附加信息
    private String issue;                // 开票人
    private String checker;              //审核人

    private String payee;                // 付款人
    private String reviewer;              //复核人
    private String sellerAccountNumber;   //银行账号
    private String        sellerBank;     //销方开户银行
    private String buyerAccountNumber;    //银行账号
    private String       buyerBank;       //购方开户银行
}
