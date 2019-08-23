package org.naruto.framework.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    private String id;

    private String nickname;

    private String avatar;

    private String profile;
}
