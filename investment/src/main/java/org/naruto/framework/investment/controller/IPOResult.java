package org.naruto.framework.investment.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IPOResult {

    private String stockCode;

    private String id;

    private String appLocation;

    private String nameCn;

    private String type;

    private Float balance = 0F;

    //申购计划
    private Integer planIPO = 0;

        //入场费
    private Float adminssionFee = 0F;

    //手续费
    private Float commissionFee = 0F;

    //认购费用
    private Float subscriptionFee = 0F;

    //申购数量
    private Integer numberOfShares;

    //中签情况
    private Integer numberOfSigned;
}
