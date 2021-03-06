package org.naruto.framework.investment.repository;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Account {
  @Id
  @GenericGenerator(name="idGenerator", strategy="uuid")
  @GeneratedValue(generator="idGenerator")
  @Column(length=40)
  @NonNull
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
  private String pinPwd;

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

  //现金可用
  private Float cash = 0F;

  //证券的资金账户，用于使用fps入金或者银证入金
  @Column(length=40)
  private String capitalAccount;


  @Transient
  private Date lastOperationAt;

  @Transient
  @Column(length=10)
  private String lastOperationStatus;

//
//  @OneToMany(targetEntity=FundTrans.class,mappedBy="account",cascade=CascadeType.ALL)
//  @Where(clause="status<4")
//  private List<FundTrans> fundTransList = new ArrayList<FundTrans>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Account account = (Account) o;

    return id.equals(account.id);
  }
}
