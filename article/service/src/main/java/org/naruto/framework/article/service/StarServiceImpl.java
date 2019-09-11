package org.naruto.framework.article.service;

import org.apache.commons.lang3.StringUtils;
import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.Star;
import org.naruto.framework.article.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StarServiceImpl implements StarService{
    @Autowired
    private StarRepository starRepository;

    public Page<Star> queryStarByPage(Map map) {
        return starRepository.queryPageByCondition(map);
    }

    @Override
    public Page<Star> queryStarByPage(StarSearchRequest searchRequest) {

        searchRequest.getPagination().setSorter("updatedAt_desc");

        Specification<Star> specification=new Specification<Star>() {
            @Override
            public Predicate toPredicate(Root<Star> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                return criteriaBuilder.equal(root.get("userId"),searchRequest.getUserId());
            }
        };

        return starRepository.findAll(specification,searchRequest.getPagination().getPageable());
    }

    @Override
    public Star queryStarByUserIdAndArticleId(String userId, String articleId) {

        return starRepository.queryStarByUserIdAndArticleId(userId,articleId);
    }

    @Override
    public Star saveStar(Star star) {

        return starRepository.save(star);
    }

    @Override
    public void deleteStar(String userId, String articleId) {

        starRepository.deleteByUserIdAndArticleId(userId,articleId);
    }
}
