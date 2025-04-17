package com.aisino.openapi.parser.model.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InvoiceType {
    SPECIAL_VAT("增值税专用发票"),
    NORMAL("普通发票"),
    ELECTRONIC_SPECIAL_VAT("电子发票(增值税专用发票)"),
    ELECTRONIC_NORMAL("电子发票(普通发票)"),
    REAL_ESTATE_RENTAL("不动产租凭"),
    AIR_PASSENGER("航空客运"),
    FREIGHT_TRANSPORT("货物运输"),
    CONSTRUCTION("建筑业发票"),
    PASSENGER_TRANSPORT("旅客运输"),
    AGRICULTURAL_PRODUCT("农产品"),
    RAILWAY_TICKET("铁路客票"),
    UNKNOWN("未知类型");

    private final String description;
}