package org.naruto.framework.investment.repository;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="stock_valuation_dashboard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class StockValuationDashborard {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    @NonNull
    private String id;
    @Column(name="stock_id")
    private String stockId;

    @Column(name="stock_name")
    private String stockName;

    @Column(name="PE")
    private Double pe;

    @Column(name="PE_type")
    private String peType;
}
