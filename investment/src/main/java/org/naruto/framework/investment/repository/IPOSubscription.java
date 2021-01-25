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
import java.util.Objects;

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
    @JoinColumn(name = "stock_id")
    @Lazy(false)
    private Stock stock;

    //认购类型（现金0、融资1）
    @Column(length=40)
    private String subscriptionType ;

    //认购数量
    private Integer numberOfShares = 0;

//    //入场费
//    private Float adminssionFee = 0F;
//
    //手续费
    private Float commissionFee = 0F;

    //利息
    private Float interest = 0F;

    //认购费用
    private Float subscriptionFee = 0F;

    //申购计划，0：未申购，1：计划申购
    private Integer planIPO = 0;

    //中签数量
    private Integer numberOfSigned = 0;
    
    @Transient
    private Date lastOperationAt;

    @Transient
    @Column(length=10)
    private String lastOperationStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IPOSubscription that = (IPOSubscription) o;
        if(Objects.equals(id, that.id)) return true;
        if(Objects.equals(account, that.account) && Objects.equals(stock, that.stock)) return true;
        return false;
    }
}
