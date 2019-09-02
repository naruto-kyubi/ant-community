package org.naruto.framework.sync.full;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.repository.ArticleRepository;
import org.naruto.framework.core.utils.ObjUtils;
import org.naruto.framework.search.article.domain.EsArticle;
import org.naruto.framework.search.article.repository.ArticleEsRepository;
import org.naruto.framework.search.user.domain.EsUser;
import org.naruto.framework.search.user.repository.UserEsRepository;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullSyncService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleEsRepository articleEsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEsRepository userEsRepository;

    public void exportArticle2ES(){
        //清除article数据;
        articleEsRepository.deleteAll();

        List<Article> articleList = articleRepository.findAll();
        List<EsArticle> esArticleList = ObjUtils.transformerClass(articleList,EsArticle.class);
        for (EsArticle esArticle : esArticleList) {
            esArticle.setUserId(esArticle.getOwner().getId());
            articleEsRepository.save(esArticle);
        }
    }

    public void exportUser2ES(){
        //清除用户数据；
        userEsRepository.deleteAll();

        List<User> userList = userRepository.findAll();
        List<EsUser> esUserList = ObjUtils.transformerClass(userList,EsUser.class);

        for (EsUser esUser : esUserList) {
            userEsRepository.save(esUser);
        }
    }
}
