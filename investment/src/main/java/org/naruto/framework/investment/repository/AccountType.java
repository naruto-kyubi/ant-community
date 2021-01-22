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
  private Integer platformFee;

  //管理费
  private Integer manageFee;

  //手续费
  private Integer commissionFee;

  // 排序字段，方便查询
  @Column(length=20)
  private String sn;

  public AccountType(String id) {
    this.id = id;
  }
}
