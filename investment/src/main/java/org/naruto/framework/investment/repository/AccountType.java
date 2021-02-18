package org.naruto.framework.investment.repository;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="account_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
public class AccountType {
  @Id
  @GenericGenerator(name="idGenerator", strategy="uuid")
  @GeneratedValue(generator="idGenerator")
  @Column(length=40)
  private String id;

  @Column(length=200)
  private String nameCn;

  @Column(length=200)
  private String nameEn;

  //平台费
  private Float platformFee;

  //管理费
  private Float manageFee;

  //现金手续费
  private Float cashCommissionFee;

  //融资手续费
  private Float financeCommissionFee;

  //手续费
//  private Float commissionFee;

  //取出资金费用
  private Float withdrawalFee;

  // 排序字段，方便查询
  @Column(length=20)
  private String sn;

  public AccountType(String id) {
    this.id = id;
  }
}
