package org.naruto.framework.investment.repository;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
public class Account {
  @Id
  @GenericGenerator(name="idGenerator", strategy="uuid")
  @GeneratedValue(generator="idGenerator")
  @Column(length=40)
  private String id;
  private String accountNo;
  private String nameCn;
  private String nameEn;
  private String mail;
  private String loginId;
  private String loginPwd;
  private String tradePwd;
  private String mobile;
  private String idCard;
  private String address;
  private String bankAccount;
  private String company;
  private String officeTel;
  private String officeAddress;
  private String owner;
  private String appLocation;
  private String type;
  private String parent;
  private Float balance;

  //证券的资金账户，用于使用fps入金或者银证入金
  private String capitalAccount;

  private Date lastOperationAt;
  private String lastOperationStatus;

}
