package org.naruto.framework.captcha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Data
@Table(name="captchas")
@NoArgsConstructor
@AllArgsConstructor
public class Captcha {
    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid")
    @GeneratedValue(generator="idGenerator")
    private String id;

    @Column(length = 20)
    @NotBlank(message = "Invalid mobile")
    private String mobile;

    @Column(length=20)
    @NotBlank(message="Invalid type")
    private String type;

    @Column(length = 20)
    @NotBlank(message = "Invalid captcha")
    private String captcha;

    @Column(name="create_at",columnDefinition="datetime")
    private Date createAt;


}
