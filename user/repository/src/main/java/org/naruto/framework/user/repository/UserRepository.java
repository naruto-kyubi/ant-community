package org.naruto.framework.user.repository;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.naruto.framework.core.repository.CustomRepository;
import org.naruto.framework.user.domain.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserRepository extends CustomRepository<User,String> {
//    public List<User> getUsersByMobile(String mobile);

    public User queryUserByMobile(String mobile);

    public List<User> queryUsersByNickname(String nickname);

    @Query(value="select u from User u where u.nickname =?1 and id <> ?2")
    public User isNicknameExist(String nickname, String id);

}
