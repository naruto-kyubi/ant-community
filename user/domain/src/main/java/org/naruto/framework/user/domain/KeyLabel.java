package org.naruto.framework.user.domain;


import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Access(AccessType.FIELD)
@Data
public class KeyLabel implements Serializable {
    private String label;
    private String key;
}