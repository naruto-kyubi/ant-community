package org.naruto.framework.investment.strategy.repository;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="strategies")
//        ,uniqueConstraints=@UniqueConstraint(columnNames={"nameEn","account_type_id","parent"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Strategy {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    @NonNull
    private String id;
    private String name;
    private String owner;
    private Date createDate;
    private String category; // 分类：股票，基金
    private String periodType; // 周期类型 “week”，“day”， “month”，“quarter”
    private Date startDate;
    private Date endDate;
    private Float realReturn; // 实盘收益
    private Float totalReturn;
    private Float annualReturn;
    private Float quarterReturn;
    private Float monthReturn;
    private Float weekReturn;
    private Float lastDayReturn;
    private Float volatility;
    private int period; // 周期天数 5
    private Float sharpeRatio;
    private Date nextTradedate; //下一交易日；
    private Float maxWithdraw; // 最大回撤；

}
