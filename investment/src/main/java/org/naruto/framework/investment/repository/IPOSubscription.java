package org.naruto.framework.investment.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="ipo_subscription")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
public class IPOSubscription {

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    private String id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "account_id")
    @Lazy(false)
    private Account account;

    //股票代码
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "stock_code")
    @Lazy(false)
    private Stock stock;

    //认购类型（现金、融资）
    @Column(length=40)
    private String subscriptionType ;

    //认购数量
    private Integer numberOfShares = 0;

    //认购费用
    private Float subscriptionFee = 0F;

    //中签数量
    private Integer numberOfSigned;

    private Date lastOperationAt = new Date();

    @Column(length=10)
    private String lastOperationStatus;

}