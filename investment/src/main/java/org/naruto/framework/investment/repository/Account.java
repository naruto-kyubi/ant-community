package org.naruto.framework.investment.repository;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="accounts")
//        ,uniqueConstraints=@UniqueConstraint(columnNames={"nameEn","account_type_id","parent"}))
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

  @Column(length=40)
  private String accountNo;

  @Column(length=40)
  private String nameCn;

  @Column(length=40)
  private String nameEn;

  @Column(length=40)
  private String mail;

  @Column(length=40)
  private String loginId;

  @Column(length=40)
  private String loginPwd;

  @Column(length=40)
  private String tradePwd;

  @Column(length=40)
  private String mobile;

  @Column(length=40)
  private String idCard;

  @Column(length=200)
  private String address;

  @Column(length=40)
  private String bankAccount;

  @Column(length=200)
  private String company;

  @Column(length=40)
  private String officeTel;

  @Column(length=200)
  private String officeAddress;

  @Column(length=40)
  private String owner;

  @Column(length=40)
  private String appLocation;

//  @Column(length=40)
//  private String type;

  @ManyToOne(fetch= FetchType.EAGER)
  @JoinColumn(name = "account_type_id")
  @Lazy(false)
  private AccountType accountType;

  @Column(length=40)
  private String parent;
  private Float balance = 0F;

  //证券的资金账户，用于使用fps入金或者银证入金
  @Column(length=40)
  private String capitalAccount;

  private Date lastOperationAt = new Date();

  @Column(length=10)
  private String lastOperationStatus;

  @OneToMany(targetEntity=FundTrans.class,mappedBy="account",cascade=CascadeType.ALL)
  private List<FundTrans> fundTransList = new ArrayList<FundTrans>();

}
