package org.naruto.framework.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="v_resource_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRole implements Serializable {

    @Id
    @Column(length = 200)
    private String resourceUrl;

    @Column(length = 400)
    private String permission;
}
