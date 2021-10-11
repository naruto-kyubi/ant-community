package org.naruto.framework.investment.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="fund_new_share_allocation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class FundNewShareAllocation {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    @NonNull
    private String id;    //_id

    private String fCode; // FCODE
    private String shortName; // SHORTNAME
    private String stockCode; // STKCODE
    private String stockName; // STKNAME
    private int sharePlace; // SHAREPLACE 获配股数
    private Float sumPlace;  // SUMPLACE  获配金额
    private Float issuePrice; // ISSUEPRICE 发行价格
    private Date placeDate; // PLACEDATE
    private Date listDate;  // LISTDATE
    private Float listPrice; // LISTPRICE 上市价格
    private Float newPrice;  //NEWPRICE    最新价
    private Float endNav;   //ENDNAV
    private Float pctNav;  // PCTNAV
    private Float discount; // DISCOUNT
    private Date pDate; // PDATE
    private Float nav; //NAV
    private Float peIssuea; // PEISSUEA
    private String isBuy; //ISBUY
    private String abbName; //ABBNAME
    private Float minSG; //MINSG 购买起点
    private Float maxSG; //MAXSG  最大可购
    private String hsgRt;  // HSGRT 获配金额占净值比例

    @Transient
    public static Map eastMoneyTransformMap = new HashMap<String,String>(){
        {
            put("_id","id");
            put("FCODE","fCode");
            put("SHORTNAME","shortName");
            put("STKCODE","stockCode");
            put("STKNAME","stockName");
            put("SHAREPLACE","sharePlace");
            put("SUMPLACE","sumPlace");
            put("ISSUEPRICE","issuePrice");
            put("PLACEDATE","placeDate");
            put("LISTDATE","listDate");
            put("LISTPRICE","listPrice");
            put("NEWPRICE","newPrice");
            put("ENDNAV","endNav");
            put("PCTNAV","pctNav");
            put("DISCOUNT","discount");
            put("PDATE","pDate");
            put("NAV","nav");
            put("PEISSUEA","peIssuea");
            put("ISBUY","isBuy");
            put("ABBNAME","abbName");
            put("MINSG","minSG");
            put("MAXSG","maxSG");
            put("HSGRT","hsgRt");
        }
    };

}
