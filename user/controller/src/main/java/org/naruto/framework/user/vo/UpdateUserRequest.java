package org.naruto.framework.user.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.naruto.framework.user.domain.Geographic;

import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserRequest {

    @NotBlank(message ="'nickname' must not be empty")
    private String nickname;

    private String email;

    private String profile;

    @Embedded
    private Geographic geographic;

    private String country;

    private String  address;
}
