package org.naruto.framework.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message ="password must not be empty")
    private String password;

    @NotBlank(message ="mobile must not be empty")
    private String mobile;

    @NotBlank(message ="captcha must not be empty")
    private String captcha;
}
