package org.naruto.framework.sync.full;

import lombok.extern.slf4j.Slf4j;
import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.ArticleStatus;
import org.naruto.framework.article.repository.ArticleRepository;
import org.naruto.framework.core.utils.ObjUtils;
import org.naruto.framework.search.article.domain.EsArticle;
import org.naruto.framework.search.article.repository.ArticleEsRepository;
import org.naruto.framework.search.user.domain.EsUser;
import org.naruto.framework.search.user.repository.UserEsRepository;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
        log.info("remove elasticsearch article data....");
        articleEsRepository.deleteAll();
        log.info("remove elasticsearch article data ok!");

        Map map = new HashMap();
        int current = 1;
        int pageSize = 5;
        int totalPage = 1;

        map.put("current",current);
        map.put("pageSize",pageSize);
        map.put("status", ArticleStatus.PUBLISH.getValue());

        Page page = articleRepository.queryPageByCondition(map);
        totalPage = page.getTotalPages();

        log.info("export article data to elasticsearch,total-elements={}",page.getTotalElements());
        while(current <= totalPage){
            List<Article> articleList =  page.getContent();
            List<EsArticle> esArticleList = ObjUtils.transformerClass(articleList,EsArticle.class);
            log.info("export article data....,pageSize={},totalPage={},current={}",pageSize,totalPage,current);
            for (EsArticle esArticle : esArticleList) {
                esArticle.setUserId(esArticle.getOwner().getId());
                articleEsRepository.save(esArticle);
            }
            current++;
            map.put("current",current);
            map.put("pageSize",pageSize);
            map.put("status", ArticleStatus.PUBLISH.getValue());
            if(current<=totalPage) page = articleRepository.queryPageByCondition(map);
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
