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

  private String nameCn;
  private String nameEn;

}