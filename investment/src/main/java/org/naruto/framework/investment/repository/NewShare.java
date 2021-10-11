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
@Table(name="new_share")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class NewShare {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    @NonNull
    private String id;    //_id
    private String stockCode; // STKCODE
    private String stockName; // STKNAME
    private Float fundNum; // FUNDNUM
    private Float sumPlace;//SUMPLACE
    private Date listDate;// LISTDATE
    private Float issuePrice; //ISSUEPRICE
    private Float listPrice; //LISTPRICE

    @Transient
    public static Map transformMap = new HashMap<String,String>() {
        {
            put("_id", "id");
            put("STKCODE", "stockCode");
            put("STKNAME", "stockName");
            put("FUNDNUM", "fundNum");
            put("SUMPLACE", "sumPlace");
            put("LISTDATE", "listDate");
            put("ISSUEPRICE", "issuePrice");
            put("LISTPRICE", "listPrice");
        }
    };
}
