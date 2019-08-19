package org.naruto.framework.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="functions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Function implements Serializable {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    @Column(length=40)
    private String id;

    @Column(length = 40)
    private String parentId;

    @Column(length = 500)
    private String path;

    @Column(length = 500)
    private String locale;

    @Column(length = 500)
    private String code;

    @Column(length = 500)
    private String name;

    @Column(length = 200)
    private String type;

    private Integer seq;
}
