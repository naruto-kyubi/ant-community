package org.naruto.framework.security.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.naruto.framework.security.domain.Role;
import org.naruto.framework.security.domain.UserRole;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends CustomRepository<UserRole,String>{

    @Query(value="select role from UserRole ur where ur.user.id=?1")
    List<Role> queryRolesByUserId(String userId);
}
