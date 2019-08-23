package org.naruto.framework.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowUserVo {

    private String id;

    private String nickname;

    private String avatar;

    private String profile;

    private String  mutual;

    private Date updatedAt;

    private String _type;

}
