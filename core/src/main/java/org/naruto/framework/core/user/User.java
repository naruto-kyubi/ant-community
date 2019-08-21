package org.naruto.framework.core.user;

import lombok.Data;


public interface User {

//    private String id;
//
//    private String nickname;
//
//    private String mobile;
//
//    private String password;

    String getId();

    void setId(String id);

    String getNickname();

    void setNickname(String nickname);

    String getMobile();

    void setMobile(String mobile);

    String getPassword();

    void setPassword(String password);
}
