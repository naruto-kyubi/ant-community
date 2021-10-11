package org.naruto.framework.investment.strategy.repository;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="portfolio_daily")
//        ,uniqueConstraints=@UniqueConstraint(columnNames={"nameEn","account_type_id","parent"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class PortfolioDaily {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    @NonNull
    private String id;

    private String portfolioId;
    private String portfolioName;
    private String portfolioType; // (stock,fund,strategy,ETF)
    private String tsCode;        // tuShare 给出的代码
    private Date tradeDate;
    private Float preClose;       // 前一日收盘
    private Float open;
    private Float high;
    private Float low;
    private Float close;
    private Float pctChg;  //涨跌幅
    private Float vol;     //  成交量 手数；
    private Float amount;  //  成交金额，千元；

}
