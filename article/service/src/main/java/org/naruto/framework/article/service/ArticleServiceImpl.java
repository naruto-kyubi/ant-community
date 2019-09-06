package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.ArticleStatus;
import org.naruto.framework.article.domain.Comment;
import org.naruto.framework.article.domain.Tag;
import org.naruto.framework.article.repository.ArticleRepository;
import org.naruto.framework.article.repository.CommentRepository;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.security.exception.SecurityError;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    public Article saveArticle(Article article,User user){
        Assert.notNull(article,"article can not be null");
        String id = article.getId();
        if(id==null) {
            if (null == user)
               throw new ServiceException(SecurityError.USER_HAS_BEEN_LOGOUT);
            if(user.getId() != article.getOwner().getId())
                throw new ServiceException(SecurityError.EXCEED_AUTHORITY_ERROR);
            return articleRepository.save(article);
        }
        else {
            Article lastVersion = null;
            if(article.getPublishedVersion()!=null && article.getStatus().equals(ArticleStatus.PUBLISH.toString())){
                lastVersion = articleRepository.findById(article.getPublishedVersion()).get();
                articleRepository.deleteById(article.getId());
            }else {
                lastVersion = articleRepository.findById(id).get();
                userService.increaseArticleCount(user.getId(),1L);
            }
            lastVersion.setCatalogId(article.getCatalogId());
            lastVersion.setContent(article.getContent());
            lastVersion.setContentHtml(article.getContentHtml());
            lastVersion.setTitle(article.getTitle());
            lastVersion.setTags(article.getTags());
            lastVersion.setStatus(article.getStatus());

            return articleRepository.save(lastVersion);
        }
    }

    public Page<Article> queryArticleByPage(Map map) {
        return articleRepository.queryPageByCondition(map);
    }

    public Article queryArticleById(String id){
        return  articleRepository.findById(id).get();
    }

    public Article queryDraftById(String id){
        Article article =  articleRepository.findArticleByPublishedVersion(id);
        if(null != article){
            return article;
        }
        article = articleRepository.findById(id).get();
        Article draft = new Article();
        List<Tag> tags = new ArrayList<Tag>();
        tags.addAll(article.getTags());
        BeanUtils.copyProperties(article,draft);
        draft.setPublishedVersion(article.getId());
        draft.setStatus(ArticleStatus.DRAFT.toString());
        draft.setId(null);
        draft.setTags(tags);
        draft = articleRepository.save(draft);
        return draft;
    }

    @Override
    public void deleteArticleById(String id) {
        articleRepository.deleteById(id);
    }

    public Comment saveComment(Comment comment){
        if(comment == null) {
            throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        }

        return commentRepository.save(comment);
    }

    public Page<Comment> queryCommentByPage(Map map) {

        return commentRepository.queryPageByCondition(map);
    }

    @Override
    public void increaseViewCount(String articleId) {

        articleRepository.increateCount(articleId,"view_count",1L);
    }

    @Override
    public void increaseLikeCount(String articleId,Long step) {

        articleRepository.increateCount(articleId,"like_count",step);
    }

    @Override
    public void increaseStarCount(String articleId,Long step) {

        articleRepository.increateCount(articleId,"star_count",step);
    }


    @Override
    public Page<Article> queryHotList(Map map) {

        return articleRepository.queryPageByCondition(map);
    }

    @Override
    public Page<Article> queryFollowArticles(Map map) {
        String currentUserId = (String) map.get("currentUserId");
        Map _map = PageUtils.prepareQueryPageMap(map);
        Pageable pageable = PageUtils.createPageable(_map);
        return articleRepository.queryArticlesByFollows(currentUserId,pageable);
    }
}
