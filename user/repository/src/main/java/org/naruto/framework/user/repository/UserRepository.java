package org.naruto.framework.user.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.naruto.framework.user.domain.User;

import java.util.List;


public interface UserRepository extends CustomRepository<User,String> {
//    public List<User> getUsersByMobile(String mobile);

    public User queryUserByMobile(String mobile);

    public List<User> queryUsersByNickname(String nickname);

}
