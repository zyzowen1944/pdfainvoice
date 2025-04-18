package com.aisino.openapi.parser.util.prompt;

public class InvoicePromptGenerator {


    /**
     * 获取不动产经营租赁服务的提示词
     * @return 提示词字符串
     */
    public static String getRealEstateLeasePrompt() {
        return "作为发票专家，我提供了一份电子普通发票的文本内容，请帮我将其解析为结构化的JSON格式，包含以下字段：" +
                "- invoiceType(发票类型): \"不动产经营租赁服务\"" +
                "- invoiceNumber(发票号码): 内容中一个20位的纯数字" +
                "- issueDate(开票日期): YYYY-MM-DD格式" +
                "- buyerName(购买方名称): 购买方全称" +
                "- buyerTaxId(购买方税号): 内容中的18位数字和字母" +
                "- sellerName(销售方名称): 销售方全称" +
                "- sellerTaxId(销售方税号): 内容中的18位数字和字母" +
                "- totalAmount(金额): 总金额，浮点小数" +
                "- taxAmount(税额): 税额，浮点小数" +
                "- totalWithTax(价税合计): 价税合计，浮点小数" +
                "- amountInWords(价税合计大写): 价税合计的大写金额" +
                "- items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax):" +
                "  - name: 项目名称" +
                "  - specification: 规格型号" +
                "  - unit: 单位" +
                "  - quantity: 数量" +
                "  - unitPrice: 单价，浮点小数" +
                "  - amount: 金额，浮点小数" +
                "  - taxRate: 税率/征收率" +
                "  - tax: 税额，浮点小数" +
                "  - propertyAddress: 不动产地址" +
                "  - leasePeriod: 租赁期限" +
                "  - tenantName: 承租方名称" +
                "  - lesseeName: 出租方名称" +
                "- issue(开票人): 开票人姓名" +
                "- remark(备注): 备注信息";
    }

    /**
     * 获取航空运输电子客票行程单的提示词
     * @return 提示词字符串
     */
    public static String getAirTransportTicketPrompt() {
        return "作为发票专家，我提供了一份电子普通发票的文本内容，请帮我将其解析为结构化的JSON格式，包含以下字段：" +
                "- invoiceType(发票类型): \"航空运输电子客票行程单\"" +
                "- invoiceNumber(发票号码): 内容中一个20位的纯数字" +
                "- issueDate(开票日期): YYYY-MM-DD格式" +
                "- buyerName(购买方名称): 购买方全称" +
                "- buyerTaxId(购买方税号): 内容中的18位数字和字母" +
                "- sellerName(销售方名称): 销售方全称" +
                "- sellerTaxId(销售方税号): 内容中的18位数字和字母" +
                "- totalAmount(金额): 总金额，浮点小数" +
                "- taxAmount(税额): 税额，浮点小数" +
                "- totalWithTax(价税合计): 价税合计，浮点小数" +
                "- amountInWords(价税合计大写): 价税合计的大写金额" +
                "- items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax):" +
                "  - name: 项目名称" +
                "  - specification: 规格型号" +
                "  - unit: 单位" +
                "  - quantity: 数量" +
                "  - unitPrice: 单价，浮点小数" +
                "  - amount: 金额，浮点小数" +
                "  - taxRate: 税率/征收率" +
                "  - tax: 税额，浮点小数" +
                "  - flightNumber: 航班号" +
                "  - departureDate: 出发日期" +
                "  - departureTime: 出发时间" +
                "  - departureAirport: 出发机场" +
                "  - arrivalAirport: 到达机场" +
                "  - passengerName: 乘客姓名" +
                "  - seatNumber: 座位号" +
                "- issue(开票人): 开票人姓名" +
                "- remark(备注): 备注信息 [<sup data-citation='{&quot;url&quot;:&quot;https://www.cnblogs.com/mxh-java/p/12469117.html&quot;,&quot;title&quot;:&quot;Java日期工具类(最全) - 尘世间迷茫的小书童 - 博客园&quot;,&quot;content&quot;:&quot;Java7和Java8日期计算工具类 import java.text.ParseException; import java.text.SimpleDateFormat; import java.time.LocalDate; import java.time.LocalDateTime; imp Java日期工具类(最全) - 尘世间迷茫的小书童 - 博客园&quot;}'>2</sup>](https://www.cnblogs.com/mxh-java/p/12469117.html)";
    }

    /**
     * 获取货物运输服务的提示词
     * @return 提示词字符串
     */
    public static String getCargoTransportPrompt() {
        return "作为发票专家，我提供了一份电子普通发票的文本内容，请帮我将其解析为结构化的JSON格式，包含以下字段：" +
                "- invoiceType(发票类型): \"货物运输服务\"" +
                "- invoiceNumber(发票号码): 内容中一个20位的纯数字" +
                "- issueDate(开票日期): YYYY-MM-DD格式" +
                "- buyerName(购买方名称): 购买方全称" +
                "- buyerTaxId(购买方税号): 内容中的18位数字和字母" +
                "- sellerName(销售方名称): 销售方全称" +
                "- sellerTaxId(销售方税号): 内容中的18位数字和字母" +
                "- totalAmount(金额): 总金额，浮点小数" +
                "- taxAmount(税额): 税额，浮点小数" +
                "- totalWithTax(价税合计): 价税合计，浮点小数" +
                "- amountInWords(价税合计大写): 价税合计的大写金额" +
                "- items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax):" +
                "  - name: 项目名称" +
                "  - specification: 规格型号" +
                "  - unit: 单位" +
                "  - quantity: 数量" +
                "  - unitPrice: 单价，浮点小数" +
                "  - amount: 金额，浮点小数" +
                "  - taxRate: 税率/征收率" +
                "  - tax: 税额，浮点小数" +
                "  - transportMode: 运输方式 (公路、铁路、海运、航空等)" +
                "  - origin: 起运地" +
                "  - destination: 目的地" +
                "  - carrier: 承运人" +
                "  - vehicleNumber: 运输工具编号" +
                "  - waybillNumber: 运单号" +
                "- issue(开票人): 开票人姓名" +
                "- remark(备注): 备注信息";
    }

    /**
     * 获取建筑服务的提示词
     * @return 提示词字符串
     */
    public static String getConstructionServicePrompt() {
        return "作为发票专家，我提供了一份电子普通发票的文本内容，请帮我将其解析为结构化的JSON格式，包含以下字段：" +
                "- invoiceType(发票类型): \"建筑服务\"" +
                "- invoiceNumber(发票号码): 内容中一个20位的纯数字" +
                "- issueDate(开票日期): YYYY-MM-DD格式" +
                "- buyerName(购买方名称): 购买方全称" +
                "- buyerTaxId(购买方税号): 内容中的18位数字和字母" +
                "- sellerName(销售方名称): 销售方全称" +
                "- sellerTaxId(销售方税号): 内容中的18位数字和字母" +
                "- totalAmount(金额): 总金额，浮点小数" +
                "- taxAmount(税额): 税额，浮点小数" +
                "- totalWithTax(价税合计): 价税合计，浮点小数" +
                "- amountInWords(价税合计大写): 价税合计的大写金额" +
                "- items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax):" +
                "  - name: 项目名称" +
                "  - specification: 规格型号" +
                "  - unit: 单位" +
                "  - quantity: 数量" +
                "  - unitPrice: 单价，浮点小数" +
                "  - amount: 金额，浮点小数" +
                "  - taxRate: 税率/征收率" +
                "  - tax: 税额，浮点小数" +
                "  - projectName: 项目名称" +
                "  - projectLocation: 项目地点" +
                "  - constructionDate: 施工日期" +
                "  - contractAmount: 合同金额，浮点小数" +
                "  - contractNumber: 合同编号" +
                "  - constructionCompany: 施工单位" +
                "- issue(开票人): 开票人姓名" +
                "- remark(备注): 备注信息";
    }

    /**
     * 获取旅客运输服务的提示词
     * @return 提示词字符串
     */
    public static String getPassengerTransportPrompt() {
        return "作为发票专家，我提供了一份电子普通发票的文本内容，请帮我将其解析为结构化的JSON格式，包含以下字段：" +
                "- invoiceType(发票类型): \"旅客运输服务\"" +
                "- invoiceNumber(发票号码): 内容中一个20位的纯数字" +
                "- issueDate(开票日期): YYYY-MM-DD格式" +
                "- buyerName(购买方名称): 购买方全称" +
                "- buyerTaxId(购买方税号): 内容中的18位数字和字母" +
                "- sellerName(销售方名称): 销售方全称" +
                "- sellerTaxId(销售方税号): 内容中的18位数字和字母" +
                "- totalAmount(金额): 总金额，浮点小数" +
                "- taxAmount(税额): 税额，浮点小数" +
                "- totalWithTax(价税合计): 价税合计，浮点小数" +
                "- amountInWords(价税合计大写): 价税合计的大写金额" +
                "- items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax):" +
                "  - name: 项目名称" +
                "  - specification: 规格型号" +
                "  - unit: 单位" +
                "  - quantity: 数量" +
                "  - unitPrice: 单价，浮点小数" +
                "  - amount: 金额，浮点小数" +
                "  - taxRate: 税率/征收率" +
                "  - tax: 税额，浮点小数" +
                "  - travelDate: 出行日期" +
                "  - seatClass: 座席类别" +
                "- issue(开票人): 开票人姓名" +
                "- remark(备注): 备注信息";
    }

    /**
     * 获取自产农产品销售的提示词
     * @return 提示词字符串
     */
    public static String getAgriculturalProductSalesPrompt() {
        return "作为发票专家，我提供了一份电子普通发票的文本内容，请帮我将其解析为结构化的JSON格式，包含以下字段：" +
                "- invoiceType(发票类型): \"自产农产品销售\"" +
                "- invoiceNumber(发票号码): 内容中一个20位的纯数字" +
                "- issueDate(开票日期): YYYY-MM-DD格式" +
                "- buyerName(购买方名称): 购买方全称" +
                "- buyerTaxId(购买方税号): 内容中的18位数字和字母" +
                "- sellerName(销售方名称): 销售方全称" +
                "- sellerTaxId(销售方税号): 内容中的18位数字和字母" +
                "- totalAmount(金额): 总金额，浮点小数" +
                "- taxAmount(税额): 税额，浮点小数" +
                "- totalWithTax(价税合计): 价税合计，浮点小数" +
                "- amountInWords(价税合计大写): 价税合计的大写金额" +
                "- items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax):" +
                "  - name: 项目名称" +
                "  - specification: 规格型号" +
                "  - unit: 单位" +
                "  - quantity: 数量" +
                "  - unitPrice: 单价，浮点小数" +
                "  - amount: 金额，浮点小数" +
                "  - taxRate: 税率/征收率" +
                "  - tax: 税额，浮点小数" +
                "  - productCategory: 农产品类别" +
                "  - harvestDate: 收获日期" +
                "  - farmerName: 农户名称" +
                "  - productionMethod: 生产方式 (有机、绿色等)" +
                "- issue(开票人): 开票人姓名" +
                "- remark(备注): 备注信息";
    }

    /**
     * 获取铁路电子客票的提示词
     * @return 提示词字符串
     */
    public static String getRailwayTicketPrompt() {
        return "作为发票专家，我提供了一份电子普通发票的文本内容，请帮我将其解析为结构化的JSON格式，包含以下字段：" +
                "- invoiceType(发票类型): \"铁路电子客票\"" +
                "- invoiceNumber(发票号码): 内容中一个20位的纯数字" +
                "- issueDate(开票日期): YYYY-MM-DD格式" +
                "- buyerName(购买方名称): 购买方全称" +
                "- buyerTaxId(购买方税号): 内容中的18位数字和字母" +
                "- sellerName(销售方名称): 销售方全称" +
                "- sellerTaxId(销售方税号): 内容中的18位数字和字母" +
                "- totalAmount(金额): 总金额，浮点小数" +
                "- taxAmount(税额): 税额，浮点小数" +
                "- totalWithTax(价税合计): 价税合计，浮点小数" +
                "- amountInWords(价税合计大写): 价税合计的大写金额" +
                "- items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax、trainNumber、departureStation,ticketNumber,passengerName,idNo):" +
                "  - name: 项目名称" +
                "  - specification: 规格型号" +
                "  - unit: 单位" +
                "  - quantity: 数量" +
                "  - unitPrice: 单价，浮点小数" +
                "  - amount: 金额，浮点小数" +
                "  - taxRate: 税率/征收率" +
                "  - tax: 税额，浮点小数" +
                "  - trainNumber: 车次" +
                "  - departureStation: 出发站" +
                "  - arrivalStation: 到达站" +
                "  - ticketNumber: 电子客票号" +
                "  - passengerName: 乘客姓名" +
                "  - idNo: 身份证号码" +
                "- issue(开票人): 开票人姓名" +
                "- remark(备注): 备注信息";
    }

    /**
     * 获取电子发票(普通发票)的提示词
     * @return 提示词字符串
     */
    public static String getElectronicGeneralInvoicePrompt() {
        return "作为发票专家，我提供了一份电子普通发票的文本内容，请帮我将其解析为结构化的JSON格式，包含以下字段：" +
                "- invoiceType(发票类型): \"电子发票(普通发票)\"" +
                "- invoiceNumber(发票号码): 内容中一个20位的纯数字" +
                "- issueDate(开票日期): YYYY-MM-DD格式" +
                "- buyerName(购买方名称): 购买方全称" +
                "- buyerTaxId(购买方税号): 内容中的18位数字和字母" +
                "- sellerName(销售方名称): 销售方全称" +
                "- sellerTaxId(销售方税号): 内容中的18位数字和字母" +
                "- totalAmount(金额): 总金额，浮点小数" +
                "- taxAmount(税额): 税额，浮点小数" +
                "- totalWithTax(价税合计): 价税合计，浮点小数" +
                "- amountInWords(价税合计大写): 价税合计的大写金额" +
                "- items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax):" +
                "  - name: 项目名称" +
                "  - specification: 规格型号" +
                "  - unit: 单位" +
                "  - quantity: 数量" +
                "  - unitPrice: 单价，浮点小数" +
                "  - amount: 金额，浮点小数" +
                "  - taxRate: 税率/征收率" +
                "  - tax: 税额，浮点小数" +
                "- issue(开票人): 开票人姓名" +
                "- remark(备注): 备注信息";
    }

    /**
     * 获取电子发票(增值税专用发票)的提示词
     * @return 提示词字符串
     */
    public static String getElectronicSpecialInvoicePrompt() {
        return "作为发票专家，我提供了一份电子增值税专用发票的文本内容，请帮我将其解析为结构化的JSON格式，包含以下字段：" +
                "- invoiceType(发票类型): \"电子发票(增值税专用发票)\"" +
                "- invoiceNumber(发票号码): 内容中一个20位的纯数字" +
                "- issueDate(开票日期): YYYY-MM-DD格式" +
                "- buyerName(购买方名称): 购买方全称" +
                "- buyerTaxId(购买方税号): 内容中的18位数字和字母" +
                "- sellerName(销售方名称): 销售方全称" +
                "- sellerTaxId(销售方税号): 内容中的18位数字和字母" +
                "- totalAmount(金额): 总金额，浮点小数" +
                "- taxAmount(税额): 税额，浮点小数" +
                "- totalWithTax(价税合计): 价税合计，浮点小数" +
                "- amountInWords(价税合计大写): 价税合计的大写金额" +
                "- items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax):" +
                "  - name: 项目名称" +
                "  - specification: 规格型号" +
                "  - unit: 单位" +
                "  - quantity: 数量" +
                "  - unitPrice: 单价，浮点小数" +
                "  - amount: 金额，浮点小数" +
                "  - taxRate: 税率/征收率" +
                "  - tax: 税额，浮点小数" +
                "- issue(开票人): 开票人姓名" +
                "- remark(备注): 备注信息";
    }

    /**
     * 获取普通发票的提示词
     * @return 提示词字符串
     */
    public static String getGeneralInvoicePrompt() {
        return "作为发票专家，我提供了一份普通发票的文本内容，请帮我将其解析为结构化的JSON格式，包含以下字段：" +
                "- invoiceType(发票类型): \"普通发票\"" +
                "- invoiceNumber(发票号码): 内容中一个20位的纯数字" +
                "- issueDate(开票日期): YYYY-MM-DD格式" +
                "- buyerName(购买方名称): 购买方全称" +
                "- buyerTaxId(购买方税号): 内容中的18位数字和字母" +
                "- sellerName(销售方名称): 销售方全称" +
                "- sellerTaxId(销售方税号): 内容中的18位数字和字母" +
                "- totalAmount(金额): 总金额，浮点小数" +
                "- taxAmount(税额): 税额，浮点小数" +
                "- totalWithTax(价税合计): 价税合计，浮点小数" +
                "- amountInWords(价税合计大写): 价税合计的大写金额" +
                "- items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax):" +
                "  - name: 项目名称" +
                "  - specification: 规格型号" +
                "  - unit: 单位" +
                "  - quantity: 数量" +
                "  - unitPrice: 单价，浮点小数" +
                "  - amount: 金额，浮点小数" +
                "  - taxRate: 税率/征收率" +
                "  - tax: 税额，浮点小数" +
                "- issue(开票人): 开票人姓名" +
                "- remark(备注): 备注信息";
    }

    /**
     * 获取增值税专用发票的提示词
     * @return 提示词字符串
     */
    public static String getSpecialInvoicePrompt() {
        return "作为发票专家，我提供了一份增值税专用发票的文本内容，请帮我将其解析为结构化的JSON格式，包含以下字段：" +
                "- invoiceType(发票类型): \"增值税专用发票\"" +
                "- invoiceNumber(发票号码): 内容中一个20位的纯数字" +
                "- issueDate(开票日期): YYYY-MM-DD格式" +
                "- buyerName(购买方名称): 购买方全称" +
                "- buyerTaxId(购买方税号): 内容中的18位数字和字母" +
                "- sellerName(销售方名称): 销售方全称" +
                "- sellerTaxId(销售方税号): 内容中的18位数字和字母" +
                "- totalAmount(金额): 总金额，浮点小数" +
                "- taxAmount(税额): 税额，浮点小数" +
                "- totalWithTax(价税合计): 价税合计，浮点小数" +
                "- amountInWords(价税合计大写): 价税合计的大写金额" +
                "- items数组(包含name、specification、unit、quantity、unitPrice、amount、taxRate、tax):" +
                "  - name: 项目名称" +
                "  - specification: 规格型号" +
                "  - unit: 单位" +
                "  - quantity: 数量" +
                "  - unitPrice: 单价，浮点小数" +
                "  - amount: 金额，浮点小数" +
                "  - taxRate: 税率/征收率" +
                "  - tax: 税额，浮点小数" +
                "- issue(开票人): 开票人姓名" +
                "- remark(备注): 备注信息";
    }
}
