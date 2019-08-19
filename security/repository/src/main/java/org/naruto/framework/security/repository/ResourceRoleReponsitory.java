package org.naruto.framework.security.repository;

import org.naruto.framework.security.domain.ResourceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ResourceRoleReponsitory extends JpaRepository<ResourceRole,String> {

    @Query(value="select DISTINCT functions.* from user_roles,role_functions,functions where user_roles.role_id=role_functions.role_id and role_functions.function_id=functions.id and user_roles.user_id=?1",nativeQuery = true)
    List<Map> queryUserFunctions(String userId);

    @Query(value="select DISTINCT functions.* from role_functions,functions where role_functions.function_id=functions.id and role_functions.role_id=?1",nativeQuery = true)
    List<Map> queryRoleFunctions(String roleId);
}
