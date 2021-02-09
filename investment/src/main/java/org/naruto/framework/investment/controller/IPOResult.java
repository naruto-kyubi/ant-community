package org.naruto.framework.investment.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IPOResult {

    private String stockId;

    private String id;

    private String appLocation;

    private String nameCn;

    private String type;

    private Float balance = 0F;

    //申购计划
    private Integer planSusbcriptionShares = 0;

        //入场费
    private Float adminssionFee = 0F;

    //手续费
    private Float commissionFee = 0F;

    //现金、融资
    private String subscriptionType = "0";

    //现金手续费
    private Float cashCommissionFee = 0F;

    //融资手续费
    private Float financeCommissionFee = 0F;

    //利息
    private Float interest = 0F;

    //认购费用
    private Float subscriptionFee = 0F;

    //申购数量
    private Integer numberOfShares;

    //中签情况
    private Integer numberOfSigned;

    private Date lastOperationAt;

    private String lastOperationStatus;
}
