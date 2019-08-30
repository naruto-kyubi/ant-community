package org.naruto.framework.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message ="mobile must not be empty")
    private String mobile;

    @NotBlank(message ="nick name must not be empty")
    private String nickname;

    @NotBlank(message ="password must not be empty")
    private String password;

    @NotBlank(message ="captcha must not be empty")
    private String captcha;
}
