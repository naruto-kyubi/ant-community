package org.naruto.framework.sync;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.Mysql2EsSyncApplication;
import org.naruto.framework.search.user.domain.EsUser;
import org.naruto.framework.search.user.repository.UserEsRepository;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.repository.UserRepository;
import org.naruto.framework.core.utils.ObjUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Mysql2EsSyncApplication.class)
public class UserEsRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEsRepository userEsRepository;

    @Test
    public void deleteAll(){
        userEsRepository.deleteAll();
    }

    @Test
    public void exportMySql2ES(){
        List<User> userList = userRepository.findAll();
        List<EsUser> esUserList = ObjUtils.transformerClass(userList,EsUser.class);

        for (EsUser esUser : esUserList) {
            userEsRepository.save(esUser);
        }
    }
}
