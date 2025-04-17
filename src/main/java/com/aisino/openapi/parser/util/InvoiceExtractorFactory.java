package com.aisino.openapi.parser.util;

import com.aisino.openapi.parser.config.AppConfig;
import com.aisino.openapi.parser.exception.ErrorCode;
import com.aisino.openapi.parser.exception.InvoiceParserException;
import com.aisino.openapi.parser.model.enums.InvoiceType;
import com.aisino.openapi.parser.util.extractors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceExtractorFactory {

    private final AppConfig appConfig;
    private final Map<InvoiceType, InvoiceExtractor> extractors = new HashMap<>();

    /**
     * 初始化所有提取器
     */
    public void init() {
      //  extractors.put(InvoiceType.ELECTRONIC_SPECIAL_VAT, new ElectronicSpecialVatExtractor(appConfig));
        extractors.put(InvoiceType.ELECTRONIC_NORMAL, new ElectronicNormalExtractor(appConfig));
        extractors.put(InvoiceType.SPECIAL_VAT, new ElectronicSpecialVATExtractor(appConfig));
       // extractors.put(InvoiceType.NORMAL, new NormalExtractor(appConfig));
      //  extractors.put(InvoiceType.REAL_ESTATE_RENTAL, new RealEstateRentalExtractor(appConfig));
      //  extractors.put(InvoiceType.AIR_PASSENGER, new AirPassengerExtractor(appConfig));
      //  extractors.put(InvoiceType.FREIGHT_TRANSPORT, new FreightTransportExtractor(appConfig));
      //  extractors.put(InvoiceType.CONSTRUCTION, new ConstructionExtractor(appConfig));
      //  extractors.put(InvoiceType.PASSENGER_TRANSPORT, new PassengerTransportExtractor(appConfig));
      //  extractors.put(InvoiceType.AGRICULTURAL_PRODUCT, new AgriculturalProductExtractor(appConfig));
      //  extractors.put(InvoiceType.RAILWAY_TICKET, new RailwayTicketExtractor(appConfig));
      //  extractors.put(InvoiceType.UNKNOWN, new GenericExtractor(appConfig));
    }

    /**
     * 获取对应发票类型的提取器
     */
    public InvoiceExtractor getExtractor(InvoiceType type) {
        if (extractors.isEmpty()) {
            init();
        }

        InvoiceExtractor extractor = extractors.get(type);
        if (extractor == null) {
            log.error("未找到发票类型对应的提取器: {}", type);
            throw new InvoiceParserException(ErrorCode.INVOICE_TYPE_UNKNOWN, "不支持的发票类型: " + type.getDescription());
        }

        return extractor;
    }
}