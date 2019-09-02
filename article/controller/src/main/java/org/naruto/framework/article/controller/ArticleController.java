package org.naruto.framework.article.controller;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.ArticleStatus;
import org.naruto.framework.article.domain.Comment;
import org.naruto.framework.article.service.ArticleService;
import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.core.session.ISessionService;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ArticleController {

    @Autowired
    private ISessionService sessionService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/v1/articles/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> add(@Validated @RequestBody Article article, HttpServletRequest request){

//        重构时放到service里面实现
//        User user = (User) sessionService.getCurrentUser(request);
//        userService.increaseArticleCount(user.getId(),1L);

        return ResponseEntity.ok(ResultEntity.ok(articleService.saveArticle(article)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/query", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> query(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        Page page = articleService.queryArticleByPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/draft/query", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryDraft(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

      //  User user = sessionUtils.getCurrentUser(request);
        Page page = articleService.queryArticleByPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }



    @ResponseBody
    @RequestMapping(value = "/v1/articles/draft/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryDraftById(@PathVariable("id") String id){
        Article draft = articleService.queryDraftById(id);
        return ResponseEntity.ok(ResultEntity.ok(draft));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryById(@PathVariable("id") String id){
        Article article = articleService.queryArticleById(id);
        return ResponseEntity.ok(ResultEntity.ok(article));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/comments", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryComments(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {
        Page page = articleService.queryCommentByPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/comment/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> addComment(@Validated @RequestBody Comment comment, HttpServletRequest request){

        User user = (User) sessionService.getCurrentUser(request);
        comment.setUserId(user);

        return ResponseEntity.ok(ResultEntity.ok(articleService.saveComment(comment)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/follows/articles", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryFollowArticles(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {
        User user = (User) sessionService.getCurrentUser(request);
        map. put("currentUserId",user.getId());
        Page page = articleService.queryFollowArticles(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    /**
     * 用户的文章；
     * @param userId
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/v1/users/{userId}/articles", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryUser2Articles(
            @PathVariable("userId") String userId,@RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {
        map.put("owner.id",userId);
        map.put("status",ArticleStatus.PUBLISH.getValue());

        Page page = articleService.queryArticleByPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/delete/{targetId}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> deleteArticle(@PathVariable("targetId") String targetId, HttpServletRequest request){
        articleService.deleteArticleById(targetId);
        return ResponseEntity.ok(ResultEntity.ok(targetId));
    }
}
