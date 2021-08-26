package org.naruto.framework.investment.repository;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name="positions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Positions {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    @NonNull
    private String id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "account_id")
    @Lazy(false)
    private Account accountId;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "stock_id")
    @Lazy(false)
    private Stock stockId;

    private Integer QTY;

    private Float price;

    private Float cost;

    private Float marketValue;

    private String currency;

}
