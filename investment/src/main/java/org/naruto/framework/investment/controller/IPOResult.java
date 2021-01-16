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

    private String appLocation;

    private String nameCn;

    private String type;

    private Float balance = 0F;

    //认购费用
    private Float subscriptionFee = 0F;

    //申购数量
    private Integer numberOfShares;

    //中签情况
    private Integer numberOfSigned;
}
