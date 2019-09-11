package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.Comment;
import org.naruto.framework.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface ArticleService {

    Article saveArticle(Article article,User user);


    Page<Article> queryArticles(ArticleSearchRequest searchRequest);

    Page<Article> queryUser2Articles(User2ArticleSearchRequest searchRequest);

    Page<Article> queryDrafts(User user);

    Article queryArticleById(String id);

    Article queryDraftById(String id);

    void deleteArticleById(String id);

    Comment saveComment(Comment comment);

    Page<Comment> queryCommentByPage(Map map);

    void increaseViewCount(String articleId);

    void increaseLikeCount(String articleId, Long step);

    void increaseStarCount(String articleId, Long step);

//    Page<ArticleVo> search(Map map);

    Page<Article> queryHotList(Map map);

    Page<Article> queryFollowArticles(Map map);

}
