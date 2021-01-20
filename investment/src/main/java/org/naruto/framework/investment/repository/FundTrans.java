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
@Table(name="fund_trans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
public class FundTrans {

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    private String id;

    //本笔转账的编码，一笔转账两条记录，分别为转出记录和转入记录，id不同，但公用一个转账编码
    @Column(length=40)
    private String transNo;

    //发生转账（出入金）的账户
    @Column(length=40)
    private String account;

    //转账类型（存入：deposit, 取出: withdraw）
    @Column(length=40)
    private String transType;

    //转账金额
    private Float amount = 0F;

    // 转账币种
    @Column(length=10)
    private String currency ;

    //转账时间
    private Date TransAt = new Date();

    private Float balanceBeforeTrans = 0F;
    private Float balanceAfterTrans = 0F;

    // 转账状态（计划：planning 执行中： processing  成功： succeed  取消： cancelled ， 完成：finished）
    @Column(length=10)
    private String status ;

}
