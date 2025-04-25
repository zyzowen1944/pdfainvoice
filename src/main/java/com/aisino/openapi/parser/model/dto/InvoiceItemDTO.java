package com.aisino.openapi.parser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

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

    // 不动产经营租赁服务
    private String propertyAddress;  // 不动产地址
    private String leasePeriod;        // 租赁期限
    private String tenantName;       // 承租方名称
    private String lesseeName;       // 出租方名称

    // 航空运输电子客票行程单
    private String flightNumber;     // 航班号
    private Date departureDate;      // 出发日期
    private String departureTime;    // 出发时间
    private String departureAirport; // 出发机场
    private String arrivalAirport;   // 到达机场
    private String passengerName;    // 乘客姓名
    private String seatNumber;       // 座位号

    // 货物运输服务
    private String transportMode;    // 运输方式 (公路、铁路、海运、航空等)
    private String origin;           // 起运地
    private String destination;      // 目的地
    private String carrier;          // 承运人
    private String vehicleNumber;    // 运输工具编号
    private String waybillNumber;    // 运单号

    // 建筑服务
    private String projectName;      // 项目名称
    private String projectLocation;  // 项目地点
    private Date constructionDate;   // 施工日期
    private BigDecimal contractAmount;// 合同金额
    private String contractNumber;   // 合同编号
    private String constructionCompany;// 施工单位

    // 旅客运输服务
    private String travelDate;       // 出行日期
    private String seatClass;        // 座席类别

    // 自产农产品销售
    private String productCategory;  // 农产品类别
    private Date harvestDate;        // 收获日期
    private String farmerName;       // 农户名称
    private String productionMethod; // 生产方式 (有机、绿色等)

    // 铁路电子客票
    private String trainNumber;      // 车次
    private String departureStation; // 出发站
    private String arrivalStation;   // 到达站
    private String ticketNumber;     // 票号
    private String idNo;             // 乘客身份证号码


    // 机动车零售发票
    // InvoiceItemDTO 补充以下字段

    // 机动车零售电子发票项目特有字段
    private String vehicleType;           // 车辆类型
    private String vehicleBrand;          // 车辆品牌
    private String vehicleModel;          // 车辆型号
    private String vehicleColor;          // 车身颜色
    private String vehicleVIN;            // 车辆识别代号/车架号
    private String vehicleEngineNumber;   // 发动机号
    private String vehicleManufacturer;   // 制造厂家
    private String vehicleTonnage;        // 吨位
    private String vehicleMaxPassengers;  // 最大载客人数
    private String vehicleDisplacement;   // 排量
    private String vehiclePower;          // 功率
    private String vehicleCertificateNumber; // 合格证号
    private String vehicleImportCertificate; // 进口证明书号
    private String vehicleTaxPaymentCertificate; // 完税证明号码
    private String vehicleUsage;          // 使用性质
    private String vehiclePurchaseDate;   // 购车日期
    private String vehicleInvoiceNumber;  // 机动车统一销售发票号码（二手车情况下）
    private String vehiclePreviousOwner;  // 前车主（二手车情况下）
    private String vehicleRegistrationNumber; // 车牌号（二手车情况下）
    private String vehicleRegistrationDate;   // 注册日期（二手车情况下）
    private String vehicleUsedYears;          // 已使用年限（二手车情况下）
    private String vehicleMileage;            // 行驶里程（二手车情况下）
    private String vehicleInspectionValidity; // 检验有效期（二手车情况下）
    private String vehicleTransferFee;        // 过户费用（二手车情况下）

}
