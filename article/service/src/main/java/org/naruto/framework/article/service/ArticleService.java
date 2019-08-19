package org.naruto.framework.article.service;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.Comment;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ArticleService {

    Article saveArticle(Article article);

    Page<Article> queryArticleByPage(Map map);

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
