package org.naruto.framework.search.search.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.naruto.framework.core.elasticsearch.ElasticSearchUtils;
import org.naruto.framework.core.elasticsearch.HighLightResultMapper;
import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.search.ElasticSearchHighlightConfig;
import org.naruto.framework.search.article.domain.EsArticle;
import org.naruto.framework.search.article.repository.ArticleEsRepository;
import org.naruto.framework.search.user.repository.UserEsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SearchEsServiceImpl implements SearchEsService{

    @Resource
    private HighLightResultMapper highLightResultMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ElasticSearchHighlightConfig elasticSearchHighlightConfig;

    @Autowired
    private UserEsRepository userEsRepository;

    @Autowired
    private ArticleEsRepository articleEsRepository;

    @Override
    public Page<Map> searchMutiIndices(Map map) {
        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        String keyWord = (String) map.get("keyword");


        SearchQuery searchQuery = new NativeSearchQueryBuilder().
                withIndices("naruto","user").
                withFields("id","nickname","profile","title","contentHtml","avatar","userId","updatedAt","tags").
                withQuery(QueryBuilders.multiMatchQuery(keyWord, "nickname","profile","title","contentHtml")).
                withHighlightFields(
                        new HighlightBuilder.Field("nickname").preTags(elasticSearchHighlightConfig.getPreTag()).postTags(elasticSearchHighlightConfig.getPostTag()),
                        new HighlightBuilder.Field("profile").preTags(elasticSearchHighlightConfig.getPreTag()).postTags(elasticSearchHighlightConfig.getPostTag()),
                        new HighlightBuilder.Field("title").preTags(elasticSearchHighlightConfig.getPreTag()).postTags(elasticSearchHighlightConfig.getPostTag()),
                        new HighlightBuilder.Field("contentHtml").preTags(elasticSearchHighlightConfig.getPreTag()).postTags(elasticSearchHighlightConfig.getPostTag())
                ).withMinScore(0.4F).
                build();
        searchQuery.setPageable(pageable);
        log.debug("searchMutiIndices:" + searchQuery.getQuery().toString());

        Page<Map> page = elasticsearchTemplate.query(searchQuery, response -> {
            SearchHits hits = response.getHits();

            List<Map> list=new ArrayList<>();
            Arrays.stream(hits.getHits()).forEach(h -> {

                Map<String, Object> source = h.getSourceAsMap();

                h.getType();
                source.put("_type",h.getType());
                //process highlight field;
                Map<String,HighlightField> mapHighLight = h.getHighlightFields();
                for (Map.Entry<String, HighlightField> fieldEntry : mapHighLight.entrySet()) {
                    String key = fieldEntry.getKey();
                    String value = ElasticSearchUtils.concat(fieldEntry.getValue().getFragments());
                    source.put(key,value);
                }
                list.add(source);
            });
            return new PageImpl(list,searchQuery.getPageable(),hits.getTotalHits());
        });
        return page;
    }

    @Override
    public Page<EsArticle> searchLikeThis(Map map) {

        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        String articleId = (String) map.get("id");
//        MoreLikeThisRequestBuilder mlt = new MoreLikeThisRequestBuilder(client, "indexName", "indexType", "id");  mlt.setField("title");//匹配的字段  SearchResponse response = client.moreLikeThis(mlt.request()).actionGet();
//        QueryBuilders.moreLikeThisQuery(new MoreLikeThisQueryBuilder.Item[]{new MoreLikeThisQueryBuilder.Item("naruto","article",articleId)});

        SearchQuery searchQuery = new NativeSearchQueryBuilder().
                withIndices("naruto").
                withFields("id","title","contentHtml","updatedAt","userId").
                withQuery(QueryBuilders.moreLikeThisQuery(
                        new MoreLikeThisQueryBuilder.Item[]{
                                new MoreLikeThisQueryBuilder.Item("naruto","article",articleId)}).
                        minimumShouldMatch("1").
                        minDocFreq(4)).
                build();
        searchQuery.setPageable(pageable);
        log.info("searchLikeThis:" + searchQuery.getQuery().toString());
        return articleEsRepository.search(searchQuery);
    }
}