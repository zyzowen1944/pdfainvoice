package com.aisino.openapi.parser.util;

/**
 * 正则表达式枚举类
 */
public enum InvoiceRegexEnum {

    /**
     * 机器编码、发票代码、发票号码、开票日期和校验码的提取正则
     */
    REGULAR_A("机器编号:(?<machineNumber>\\d{12})|发票代码:(?<invoiceCode>\\d{12})|发票号码:(?<invoiceNumber>\\d{8})|:(?<issueDate>\\d{4}年\\d{2}月\\d{2}日)|校验码:(?<checksum>\\d{20}|\\S{4,})"),

    /**
     * 新版发票的机器编码、发票代码、发票号码、开票日期和校验码的提取正则
     */
    REGULAR_A_NEW("发票号码:(?<invoiceNumber>\\d{20})|:(?<issueDate>\\d{4}年\\d{2}月\\d{2}日)|(售名称|销名称):(?<buyerName>\\S*)"),

    /**
     * 发票号码备用提取正则
     */
    REGULAR_A_1("(国制|制普通发票)(?<invoiceNumber>\\d{8})"),

    /**
     * 发票号码跨行提取正则
     */
    REGULAR_A_1R("发票号码:(?<invoiceNumber>\\d{7})[\\s\\S]*?(\\d+)"),

    /**
     * 开票日期备用提取正则
     */
    REGULAR_A_2("开票日期:(?<issueDate>\\d{4}\\d{2}月\\d{2}日)"),

    /**
     * 发票代码备用提取正则
     */
    REGULAR_A_3("发票代码(?<invoiceCode>\\d{12})"),

    /**
     * 发票代码跨行提取正则
     */
    REGULAR_A_3R("发票代码:(?<invoiceCode>\\d{10})[\\s\\S]*?(\\d+)"),

    /**
     * 金额、税额提取正则，匹配形如 "合计¥?金额¥?税额" 的文本
     */
    REGULAR_B("合计¥?(?<totalAmount>[^ \\f\\n\\r\\t\\v*]*)(?:¥?(?<totalTax>\\S*)|\\*+)\\s"),

    /**
     * 金额提取正则，用于匹配结果有误的修正
     */
    REGULAR_BR("合计¥(?<totalAmount>\\d+\\.\\d+)"),

    /**
     * 金额、税额备用提取正则
     */
    REGULAR_B_1("合\\u0020*计\\u0020*¥?(?<totalAmount>[^ ]*)\\u0020+¥?(?:(?<totalTax>\\S*)|\\*+)\\s"),

    /**
     * 价税合计提取正则，匹配"价税合计（大写）XXX（小写）¥YYY"格式的文本
     */
    REGULAR_C("价税合计\\u0028大写\\u0029(?<amountInWords>\\S*)\\u0028小写\\u0029¥?(?<totalWithTax>\\S*)\\s"),

    /**
     * 收款人、复核、开票人、销售方提取正则，匹配格式为"收款人:xxx复核:xxx开票人:xxx销售方"的字符串
     */
    REGULAR_D("收款人:(?<payee>\\S*)复核:(?<reviewer>\\S*)开票人:(?<issue>\\S*)销售方"),

    /**
     * 发票类型提取正则，匹配"xxx通发票"格式的发票类型
     */
    REGULAR_E("(?<invoiceType>\\S*)通发票"),

    /**
     * 发票类型提取正则，匹配"xxx用发票"格式的发票类型
     */
    REGULAR_E_1("(?<invoiceType>\\S*)用发票"),

    /**
     * 发票类型提取 - 辅助正则
     */
    REGULAR_E_AUX("(?:国|统|一|发|票|监|制)"),

    /**
     * 购买方信息提取正则
     */
    REGULAR_F("名称:(?<buyerName>\\S*)|纳税人识别号:(?<buyerTaxId>\\S*)|地址、电话:(?<buyerAddress>\\S*)|开户行及账号:(?<buyerAccount>\\S*)|电子支付标识:(?<buyerPaymentId>\\S*)"),

    /**
     * 针对深圳发票的销售方名称提取正则
     */
    REGULAR_FR("名称:(?<sellerName>\\S*)"),

    /**
     * 处理除了金额和税额之外的其他文本元素正则
     */
    REGULAR_G("^(-?\\d+)(\\.\\d+)?$"),

    /**
     * 备注信息提取正则
     */
    REGULAR_A_NOTE_BUYER("购方开户银行:(?<buyerBank>[^;]+);"),
    REGULAR_A_NOTE_BUYERACCOUNT("银行账号:(?<buyerAccountNumber>\\d+)(?=[,;])"),
    REGULAR_A_NOTE_SELLER("销方开户银行:(?<sellerBank>.*?)(?=[,;]|\\Z)"),
    REGULAR_A_NOTE_SELLERACCOUNT("银行账号:(?<sellerAccountNumber>\\d+)(?=[,;]|\\Z)"),
    REGULAR_A_NOTE_PAYEE("收款人:(?<payee>.*?)(?=[,;]|\\Z)"),
    REGULAR_A_NOTE_CHECKER("复核人:(?<reviewer>.*?)(?=[,;]|\\Z)"),

    /**
     * 检查当前详细项字符串是否符合特定条件正则
     */
    REGULAR_H("\\S+\\d*(%|免税|不征税|出口零税率|普通零税率)\\S*"),
    REGULAR_H_1("^ *\\d*(%|免税|不征税|出口零税率|普通零税率)\\S*"),
    REGULAR_H_2("\\S+\\d+%[\\-\\d]+\\S*"),
    REGULAR_H_3("^ *\\d*(%|免税|不征税|出口零税率|普通零税率)\\S*");


    private final String regex;


    InvoiceRegexEnum(String regex) {
        this.regex = regex;
    }


    public String getRegex() {
        return regex;
    }
}
