package org.naruto.framework.article.controller;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.Like;
import org.naruto.framework.article.service.ArticleService;
import org.naruto.framework.article.service.LikeService;
import org.naruto.framework.article.vo.LikeVo;
import org.naruto.framework.core.security.SessionUtils;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionUtils sessionUtils;

    @ResponseBody
    @RequestMapping(value = "/v1/articles/{type}/{targetId}/likes", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryLikeById(@PathVariable("type") String type, @PathVariable("targetId") String targetId, HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        Like like = null;
        if(null!=user){
            like = likeService.queryLikeByUserIdAndTypeAndTargetId(user.getId(),type,targetId);
        }
        Article article = articleService.queryArticleById(targetId);
        LikeVo vo = new LikeVo(like,article.getLikeCount());
        return ResponseEntity.ok(ResultEntity.ok(vo));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/likes/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> addLike(@Validated @RequestBody Like like, HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);

        articleService.increaseLikeCount(like.getTargetId(),1L);
        userService.increaseLikeCount(user.getId(),1L);

        like.setUserId(user.getId());
        likeService.saveLike(like);
        Article article = articleService.queryArticleById(like.getTargetId());
        LikeVo vo = new LikeVo(like,article.getLikeCount());

        return ResponseEntity.ok(ResultEntity.ok(vo));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/{type}/{targetId}/likes/delete", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> deleteLike(@PathVariable("type") String type, @PathVariable("targetId") String targetId, HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        likeService.deleteLike(user.getId(),type,targetId);
        userService.increaseLikeCount(user.getId(),-1L);

        articleService.increaseLikeCount(targetId,-1L);
        Article article = articleService.queryArticleById(targetId);
        LikeVo vo = new LikeVo(null,article.getLikeCount());

        return ResponseEntity.ok(ResultEntity.ok(vo));
    }

}
