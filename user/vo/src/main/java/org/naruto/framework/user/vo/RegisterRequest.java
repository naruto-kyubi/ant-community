package org.naruto.framework.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message ="mobile is blank")
    private String mobile;

    @NotBlank(message ="nick name is blank")
    private String nickname;

    @NotBlank(message ="password is blank")
    private String password;

    @NotBlank(message ="captcha is blank")
    private String captcha;
}
