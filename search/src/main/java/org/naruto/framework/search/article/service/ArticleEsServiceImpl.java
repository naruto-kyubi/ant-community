package org.naruto.framework.search.article.service;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.naruto.framework.article.vo.ArticleVo;
import org.naruto.framework.core.elasticsearch.HighLightResultMapper;
import org.naruto.framework.core.utils.ObjUtils;
import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.search.ElasticSearchHighlightConfig;
import org.naruto.framework.search.article.domain.EsArticle;
import org.naruto.framework.search.article.repository.ArticleEsRepository;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleEsServiceImpl implements ArticleEsService{

    @Autowired
    private ArticleEsRepository articleEsRepository;

    @Autowired
    private UserService userService;

    @Resource
    private HighLightResultMapper highLightResultMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ElasticSearchHighlightConfig elasticSearchHighlightConfig;

    @Override
    public Page<ArticleVo> search(Map map) {

        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        String keyWord = (String) map.get("keyword");

        SearchQuery searchQuery = new NativeSearchQueryBuilder().
                withQuery(QueryBuilders.multiMatchQuery(keyWord, "title","contentHtml")).
        withHighlightFields(
            new HighlightBuilder.Field("title").preTags(elasticSearchHighlightConfig.getPreTag()).postTags(elasticSearchHighlightConfig.getPostTag()),
            new HighlightBuilder.Field("contentHtml").preTags(elasticSearchHighlightConfig.getPreTag()).postTags(elasticSearchHighlightConfig.getPostTag())
        ).withMinScore(0.9F).
                        build();
        searchQuery.setPageable(pageable);
        Page<EsArticle> page = elasticsearchTemplate.queryForPage(searchQuery, EsArticle.class, highLightResultMapper);

//        Page page = articleEsRepository.search(searchQuery,);
// 高亮字段
//        AggregatedPage<EsArticle> page = elasticsearchTemplate.queryForPage(searchQuery, EsArticle.class, new SearchResultMapper() {
//
//            @Override
//            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
//                List<EsArticle> chunk = new ArrayList<>();
//                for (SearchHit searchHit : response.getHits()) {
//                    if (response.getHits().getHits().length <= 0) {
//                        return null;
//                    }
//                    EsArticle idea = new EsArticle();
//                    //name or memoe
//                    HighlightField ideaTitle = searchHit.getHighlightFields().get("title");
//                    if (ideaTitle != null) {
//                        idea.setTitle(ideaTitle.fragments()[0].toString());
//                    }
//                    HighlightField ideaContent = searchHit.getHighlightFields().get("contentHtml");
//                    if (ideaContent != null) {
//                        idea.setContentHtml(ideaContent.fragments()[0].toString());
//                    }
//
//                    chunk.add(idea);
//                }
//                if (chunk.size() > 0) {
//                    return new AggregatedPageImpl<>((List<T>) chunk);
//                }
//                return null;
//            }
//        });

//        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery(keyWord, "title","content").fields(searchMap);
//        query.minimumShouldMatch("90%");
//        Page<EsArticle> page = articleEsRepository.search(query,pageable);
        List<EsArticle> list = page.getContent();

        List voList = list.stream().map(item-> {
                String userId = item.getUserId();
                User user = userService.queryUserById(userId);
                item.setOwner(user);
                return ObjUtils.convert(item,ArticleVo.class);
            }).collect(Collectors.toList());

        return new PageImpl(voList,page.getPageable(),page.getTotalElements());
    }

    @Override
    public EsArticle save(EsArticle esArticle) {

        return articleEsRepository.save(esArticle);
    }

    @Override
    public void delete(String id) {

        articleEsRepository.deleteById(id);
    }
}
