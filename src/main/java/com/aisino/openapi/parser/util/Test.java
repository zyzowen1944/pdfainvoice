package com.aisino.openapi.parser.util;

import com.aisino.openapi.parser.model.dto.InvoiceDTO;
import com.aisino.openapi.parser.model.enums.InvoiceType;
import com.alibaba.fastjson2.JSONObject;

public class Test {

    public static void main(String[] args) {
        String s= "```json\n" +
                "{\n" +
                "  \"invoiceType\": \"普通发票\",\n" +
                "  \"invoiceNumber\": \"25337000000124583791\",\n" +
                "  \"invoiceDate\": \"2025年04月03日\",\n" +
                "  \"buyerName\": \"永嘉县人民医院（永嘉县东城街道社区卫生服务中心）\",\n" +
                "  \"buyerTaxId\": \"12330324470726738X\",\n" +
                "  \"sellerName\": \"上药控股温州有限公司\",\n" +
                "  \"sellerTaxId\": \"9133030477313537XX\",\n" +
                "  \"totalAmount\": 1680.00,\n" +
                "  \"taxAmount\": 193.27,\n" +
                "  \"totalWithTax\": 1873.27,\n" +
                "  \"amountInWords\": \"壹仟陆佰捌拾圆整\",\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"name\": \"中成药*小儿肺热咳喘颗粒4g(相当于饮片10.6g)*12袋盒\",\n" +
                "      \"specification\": \"4g(相当于饮片10.6g)\",\n" +
                "      \"unit\": \"盒\",\n" +
                "      \"quantity\": 50,\n" +
                "      \"unitPrice\": 29.73451327,\n" +
                "      \"amount\": 1486.73,\n" +
                "      \"taxRate\": 13.00,\n" +
                "      \"tax\": 193.27\n" +
                "    }\n" +
                "  ],\n" +
                "  \"issue\": \"开票人：林陈菊\",\n" +
                "  \"remark\": \"销方开户银行:中信银行温州分行营业部;    银行账号:8110801012702471874;   销售订单NO:501202504030549 O13300002025040211890, (城南社区)永嘉县东城街道浦口村\"\n" +
                "}\n" +
                "```";

        InvoiceDTO invoice = new InvoiceDTO();


        // 提取基本信息
        //extractBasicInfo(invoice, text);

        // 提取明细项目
        //extractItems(invoice, text);
        s = s.substring(s.indexOf("{"),s.lastIndexOf("}")+1);
        System.out.println(s);
        invoice = JSONObject.parseObject(s,InvoiceDTO.class);
        invoice.setInvoiceType(InvoiceType.ELECTRONIC_NORMAL);

        System.out.println(invoice.toString());
    }
}
