package org.naruto.framework.investment.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
public class Stock {

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    private String id;

    //股票代码
    @Column(length=10)
    private String code;

    //股票名称
    @Column(length=100)
    private String name;

    //一手数量
    private Integer lot = 0;

    //入场费
    private Float admissionFee = 0F;

    //招股开始日期
    private Date applicationFromDate;

    //招股结束日期
    private Date applicationToDate;

    //公布配售结果日期
    private Date allotmentResultAnnounce;

    //上市日期
    private Date listingDate;

    //所属行业
    @Column(length=100)
    private String industry;

    //总发售数量
    private Float totalIssueAmount = 0F;

    //保荐人
    @Column(length=100)
    private String sponsor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,code, name, lot, admissionFee, applicationFromDate, applicationToDate, allotmentResultAnnounce, listingDate, industry, totalIssueAmount, sponsor);
    }
}
