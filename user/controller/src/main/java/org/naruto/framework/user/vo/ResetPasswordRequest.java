package org.naruto.framework.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message ="password is blank")
    private String password;

    @NotBlank(message ="mobile is blank")
    private String mobile;

    @NotBlank(message ="captcha is blank")
    private String captcha;
}
