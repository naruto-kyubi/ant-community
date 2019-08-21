package org.naruto.framework.core.user;



public interface ThirdPartyUser {

    String getId();

    void setId(String id);

    String getAuthType() ;

    void setAuthType(String authType) ;

    String getUid() ;

    void setUid(String uid) ;

    String getName() ;

    void setName(String name) ;
}
