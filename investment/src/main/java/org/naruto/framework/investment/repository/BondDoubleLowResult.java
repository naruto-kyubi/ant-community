package org.naruto.framework.investment.repository;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="convertible_bond_earning_stat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(value={AuditingEntityListener.class})
@ToString
public class BondDoubleLowResult {

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    private String id;
    private Date earningDate;
    private Double earningRate;
}
