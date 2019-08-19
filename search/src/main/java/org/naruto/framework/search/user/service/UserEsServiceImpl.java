package org.naruto.framework.search.user.service;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.naruto.framework.core.elasticsearch.HighLightResultMapper;
import org.naruto.framework.core.utils.ObjUtils;
import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.search.ElasticSearchHighlightConfig;
import org.naruto.framework.search.user.domain.EsUser;
import org.naruto.framework.search.user.repository.UserEsRepository;
import org.naruto.framework.user.vo.UserVo;
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

@Service
public class UserEsServiceImpl implements UserEsService {

    @Resource
    private HighLightResultMapper highLightResultMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ElasticSearchHighlightConfig elasticSearchHighlightConfig;

    @Autowired
    private UserEsRepository userEsRepository;

    @Override
    public Page<UserVo> search(Map map) {

        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        String keyWord = (String) map.get("keyword");

        SearchQuery searchQuery = new NativeSearchQueryBuilder().
                withQuery(QueryBuilders.multiMatchQuery(keyWord, "nickname","profile")).
                withHighlightFields(
                        new HighlightBuilder.Field("nickname").preTags(elasticSearchHighlightConfig.getPreTag()).postTags(elasticSearchHighlightConfig.getPostTag()),
                        new HighlightBuilder.Field("profile").preTags(elasticSearchHighlightConfig.getPreTag()).postTags(elasticSearchHighlightConfig.getPostTag())
                ).withMinScore(0.4F).
                build();
        searchQuery.setPageable(pageable);
        Page<EsUser> page = elasticsearchTemplate.queryForPage(searchQuery, EsUser.class, highLightResultMapper);
        List<EsUser> userList = page.getContent();

        List voList = ObjUtils.transformerClass(userList, UserVo.class);
        return new PageImpl(voList,page.getPageable(),page.getTotalElements());
    }

    @Override
    public void delete(String id) {

        userEsRepository.deleteById(id);
    }

    @Override
    public EsUser save(EsUser esUser) {

        return userEsRepository.save(esUser);
    }
}