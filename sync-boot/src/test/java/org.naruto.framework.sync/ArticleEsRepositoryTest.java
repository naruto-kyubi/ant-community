package org.naruto.framework.sync;

import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.naruto.framework.Mysql2EsSyncApplication;
import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.repository.ArticleRepository;
import org.naruto.framework.search.article.domain.EsArticle;
import org.naruto.framework.search.article.repository.ArticleEsRepository;
import org.naruto.framework.core.utils.ObjUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Mysql2EsSyncApplication.class)
public class ArticleEsRepositoryTest {

    @Autowired
    private ArticleEsRepository articleEsRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void testAdd(){
        EsArticle ar1 = new EsArticle();
        ar1.setId("1");
        ar1.setTitle("数问");
        ar1.setContent("问得好");
        ar1.setContentHtml("test conent html");
        ar1.setCover("te");
        ar1.setUserId("4028838e6abfb595016abfb60e4d0000");

        EsArticle ar2 = new EsArticle();
        ar2.setId("2");
        ar2.setTitle("问题");
        ar2.setContent("数据问题");
        ar2.setContentHtml("test conent html");
        ar2.setCover("te");
        ar2.setUserId("4028838e6abfb595016abfb60e4d0000");

        EsArticle ar3 = new EsArticle();
        ar3.setId("3");
        ar3.setTitle("数据的问题");
        ar3.setContent("数据的问题");
        ar3.setContentHtml("test 数据 html");
        ar3.setCover("te");
        ar3.setUserId("4028838e6abfb595016abfb60e4d0000");

        EsArticle ar4 = new EsArticle();
        ar4.setId("4");
        ar4.setTitle("数据艺术");
        ar4.setContent("数据的艺术");
        ar4.setContentHtml("test 数据 html");
        ar4.setCover("te");
        ar4.setUserId("4028838e6abfb595016abfb60e4d0000");

        EsArticle ar5 = new EsArticle();
        ar5.setId("4");
        ar5.setTitle("不知道的问题");
        ar5.setContent("不知");
        ar5.setContentHtml("test 数据 html");
        ar5.setCover("te");
        ar5.setUserId("4028838e6abfb595016abfb60e4d0000");

        articleEsRepository.save(ar1);
        articleEsRepository.save(ar2);
        articleEsRepository.save(ar3);
        articleEsRepository.save(ar4);
        articleEsRepository.save(ar5);

    }

    @Test
    public void testUpdate(){

        EsArticle ar = articleEsRepository.findById("2").get();
        ar.setTitle("update ....");
        articleEsRepository.save(ar);
    }

    @Test
    public void testDelete(){

        articleEsRepository.deleteById("2");
    }

    @Test
    public void testQuery(){
        List<EsArticle> articles  = articleEsRepository.findArticlesByTitleLike("title");
        for (EsArticle article : articles) {
            System.out.println(article.toString());
        }
    }

    @Test
    public void testSearch(){
        String query ="title";
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.queryStringQuery(query));
            //分页设置
            searchSourceBuilder.from(0).size(2);
            System.out.println("全文搜索查询语句:"+searchSourceBuilder.toString());
            Iterable<EsArticle> it =  articleEsRepository.search(searchSourceBuilder.query());
            it.forEach(article -> {
                System.out.println(article.toString());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //多个字段匹配（模糊分词查询）
    @Test
    public void testMultiMatchQuery(){
        String keyWord = "数据问题";
//        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery(keyWord, "title","content");
        Map map = new HashMap();
        map.put("title",1F);
        map.put("content",1F);

        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery(keyWord, "title","content").fields(map);

        query.minimumShouldMatch("90%");
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(query);
        int currentPage = 0;
        int pageSize = 2;
        Pageable pageable = new PageRequest(currentPage, pageSize);
        System.out.println(query.toString());
        Page<EsArticle> page = articleEsRepository.search(query,pageable);
        for (EsArticle article : page.getContent()) {
            System.out.println(article);
        }

    }

    @Test
    public void deleteAll(){
        articleEsRepository.deleteAll();
    }

    @Test
    public void exportMySql2ES(){
        List<Article> articleList = articleRepository.findAll();
        List<EsArticle> esArticleList = ObjUtils.transformerClass(articleList,EsArticle.class);
        for (EsArticle esArticle : esArticleList) {
            esArticle.setUserId(esArticle.getOwner().getId());
            articleEsRepository.save(esArticle);
        }

    }
}
