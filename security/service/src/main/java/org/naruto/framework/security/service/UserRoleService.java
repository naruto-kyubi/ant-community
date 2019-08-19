package org.naruto.framework.security.service;

import org.naruto.framework.security.domain.Role;
import org.naruto.framework.security.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;

    public List<Role> queryRolesByUserId(String userId){

        return userRoleRepository.queryRolesByUserId(userId);
    }
}
