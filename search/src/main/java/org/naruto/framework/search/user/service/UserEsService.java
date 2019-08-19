package org.naruto.framework.search.user.service;

import org.naruto.framework.search.user.domain.EsUser;
import org.naruto.framework.user.vo.UserVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface UserEsService {

    Page<UserVo> search(Map map);

    void delete(String id);

    EsUser save(EsUser esUser);
}
