package com.aisino.openapi.parser.service;

import com.aisino.openapi.parser.model.enums.InvoiceType;
import com.aisino.openapi.parser.util.prompt.InvoicePromptGenerator;
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

    public String search(InvoiceType invoiceType, String prompt) {
        String msg = "";

        switch (invoiceType.getDescription()){

            case "增值税专用发票":
                msg = InvoicePromptGenerator.getSpecialInvoicePrompt();
                break;
            case "普通发票":
                msg = InvoicePromptGenerator.getGeneralInvoicePrompt() ;
                break;
            case "电子发票(增值税专用发票)":
                msg = InvoicePromptGenerator.getElectronicSpecialInvoicePrompt() ;
                break;
            case "电子发票(普通发票)":
                msg = InvoicePromptGenerator.getElectronicGeneralInvoicePrompt() ;
                break;
            case "不动产租凭":
                msg = InvoicePromptGenerator.getRealEstateLeasePrompt() ;
                break;
            case "航空客运":
                msg = InvoicePromptGenerator.getAirTransportTicketPrompt();
                break;
            case "货物运输":
                msg = InvoicePromptGenerator.getCargoTransportPrompt();
                break;
            case "建筑业发票":
                msg = InvoicePromptGenerator.getConstructionServicePrompt();
                break;
            case "旅客运输":
                msg = InvoicePromptGenerator.getPassengerTransportPrompt();
                break;
            case "农产品":
                msg = InvoicePromptGenerator.getAgriculturalProductSalesPrompt();
                break;
            case "铁路客票":
                msg = InvoicePromptGenerator.getRailwayTicketPrompt();
                break;
            default:
                msg = InvoicePromptGenerator.getElectronicGeneralInvoicePrompt();
        }

        return ollamaChatModel.call(msg+prompt);
    }
}