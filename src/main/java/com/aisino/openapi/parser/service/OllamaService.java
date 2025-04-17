package com.aisino.openapi.parser.service;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService {

    private final OllamaChatModel ollamaChatModel;

    public OllamaService(OllamaChatModel ollamaChatModel) {
        this.ollamaChatModel = ollamaChatModel;
    }

    public String search(String prompt) {
        String msg = "作为发票专家，我提供了一份电子普通发票的文本内容，请帮我将其解析为结构化的JSON格式，"
                + "包含以下字段：invoiceType(发票类型)、invoiceNumber(发票号码)、issueDate(开票日期)、"
                + "buyerName(购买方名称)、buyerTaxId(购买方税号)、sellerName(销售方名称)、"
                + "sellerTaxId(销售方税号)、totalAmount(金额)、taxAmount(税额)、"
                + "totalWithTax(价税合计)、amountInWords(价税合计大写)、"
                + "items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax)、"
                + "以及issue(开票人)和remark(备注)。\n\n"
                + "请注意：invoiceNumber发票号码是内容中一个20位的纯数字，购买和销售方税号是内容中的18位的数字和字母，如果免税商品，税额为0" +
                 "以下是发票文本内容：\n\n" + prompt;


        return ollamaChatModel.call(msg);
    }
}