package com.aisino.openapi.parser.util;

import com.aisino.openapi.parser.model.dto.InvoiceDTO;
import com.aisino.openapi.parser.model.enums.InvoiceType;
import com.alibaba.fastjson2.JSONObject;

public class Test {

    public static void main(String[] args) {
        String s= "{\n" +
                "  \"invoiceType\": \"电子普通发票\",\n" +
                "  \"invoiceNumber\": \"24112000000219407560\",\n" +
                "  \"issueDate\": \"2024年12月23日\",\n" +
                "  \"buyerName\": \"北京轻舟装饰材料有限公司\",\n" +
                "  \"buyerTaxId\": \"91110115MADLN4H98C\",\n" +
                "  \"sellerName\": \"北京英格卡购物中心有限公司\",\n" +
                "  \"sellerTaxId\": \"91110000692300855B\",\n" +
                "  \"totalAmount\": 571.43,\n" +
                "  \"taxAmount\": 28.57,\n" +
                "  \"totalWithTax\": 600.00,\n" +
                "  \"amountInWords\": \"陆佰圆整\",\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"name\": \"*经营租赁*停车费\",\n" +
                "      \"specification\": \"京（2023）大不动产权第 m²\",\n" +
                "      \"unit\": \"\",\n" +
                "      \"quantity\": 2,\n" +
                "      \"unitPrice\": 285.714285714286,\n" +
                "      \"amount\": 571.43,\n" +
                "      \"taxRate\": 5 / 100,\n" +
                "      \"tax\": 28.57\n" +
                "    }\n" +
                "  ],\n" +
                "  \"issue\": \"王彩霞\",\n" +
                "  \"remark\": \"\"\n" +
                "}";

        String msg = "作为发票专家，我提供了一份电子普通发票的文本内容，请帮我将其解析为结构化的JSON格式，"
                + "包含以下字段：invoiceType(发票类型)、invoiceNumber(发票号码)、issueDate(开票日期)、"
                + "buyerName(购买方名称)、buyerTaxId(购买方税号)、sellerName(销售方名称)、"
                + "sellerTaxId(销售方税号)、totalAmount(金额)、taxAmount(税额)、"
                + "totalWithTax(价税合计)、amountInWords(价税合计大写)、"
                + "items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax)、"
                + "以及issue(开票人)和remark(备注)。\n\n"
                + "请注意：保证tax税额字段、unitPrice、amount都是浮点小数，invoiceNumber发票号码是内容中一个20位的纯数字，购买和销售方税号是内容中的18位的数字和字母，如果免税商品，税额为0" +
                "以下是发票文本内容：\n\n" ;

        System.out.println(msg);
        InvoiceDTO invoice = new InvoiceDTO();


        // 提取基本信息
        //extractBasicInfo(invoice, text);

        // 提取明细项目
        //extractItems(invoice, text);
       // s = s.substring(s.indexOf("{"),s.lastIndexOf("}")+1);
        System.out.println(s);
        invoice = JSONObject.parseObject(s,InvoiceDTO.class);
        invoice.setInvoiceType(InvoiceType.ELECTRONIC_NORMAL);

        System.out.println(invoice.toString());
    }
}
