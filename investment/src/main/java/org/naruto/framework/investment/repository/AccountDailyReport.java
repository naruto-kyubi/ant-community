package org.naruto.framework.investment.repository;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="accounts_daily_report")
//        ,uniqueConstraints=@UniqueConstraint(columnNames={"nameEn","account_type_id","parent"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
public class AccountDailyReport {
  @Id
  @GenericGenerator(name="idGenerator", strategy="uuid")
  @GeneratedValue(generator="idGenerator")
  @Column(length=40)
  private String id;

  @Column(length=40)
  private String nameCn;

  @Column(length=40)
  private String nameEn;

  @Column(length=40)
  private String mail;

  @Column(length=40)
  private String mobile;

  @Column(length=40)
  private String owner;

  @Column(length=40)
  private String appLocation;

  private Date statisticAt;

  private Float balance = 0F;

}
