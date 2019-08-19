package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Star;
import org.naruto.framework.article.repository.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
