package org.naruto.framework.investment.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name="fund_trans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class FundTrans {

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    private String id;

    // 发起账户，必须是券商，不能是银行，根据这个字段进行查询
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "account")
    @Lazy(false)
    private Account account;

    //转出账户
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "debit_account")
    @Lazy(false)
    private Account debitAccount;

    //转入账户
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="receiving_account")
    @Lazy(false)
    private Account receivingAccount;

    @Transient
    private Integer transType;

    //转账金额
    private Float amount = 0F;

    // 转账币种
    @Column(length=10)
    private String currency ;

    //转账时间
    private Date transAt = new Date();

    private Float balanceBeforeTransOfDebitAccount = 0F;
    private Float balanceBeforeTransOfReceivingAccount = 0F;

    // 转账状态（计划：planning 执行中： processing  成功： succeed  取消： cancelled ， 完成：finished）
    @Column(length=10)
    private Integer status ;

}
