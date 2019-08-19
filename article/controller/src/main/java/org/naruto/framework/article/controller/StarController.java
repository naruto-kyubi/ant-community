package org.naruto.framework.article.controller;

import org.naruto.framework.article.domain.Article;
import org.naruto.framework.article.domain.Star;
import org.naruto.framework.article.service.ArticleService;
import org.naruto.framework.article.service.StarService;
import org.naruto.framework.article.vo.StarVo;
import org.naruto.framework.core.security.SessionUtils;
import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class StarController {

    @Autowired
    private StarService starService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionUtils sessionUtils;

    @ResponseBody
    @RequestMapping(value = "/v1/articles/stars", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryStar(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        Page page = starService.queryStarByPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/{articleId}/stars", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryStarById(@PathVariable("articleId") String articleId, HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        Star star = null;
        if(null!=user) {
            star = starService.queryStarByUserIdAndArticleId(user.getId(), articleId);
        }
        Article article = articleService.queryArticleById(articleId);
        StarVo vo = new StarVo(star,article.getStarCount());

        return ResponseEntity.ok(ResultEntity.ok(vo));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/stars/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> addStar(@Validated @RequestBody Star star, HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        star.setUserId(user.getId());

        articleService.increaseStarCount(star.getArticle().getId(),1L);
        userService.increaseStarCount(user.getId(),1L);

        starService.saveStar(star);
        Article article = articleService.queryArticleById(star.getArticle().getId());
        StarVo vo = new StarVo(star,article.getStarCount());

        return ResponseEntity.ok(ResultEntity.ok(vo));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/{id}/stars/delete", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> deleteStar(@PathVariable("id") String id, HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);

        articleService.increaseStarCount(id,-1L);
        userService.increaseStarCount(user.getId(),-1L);

        starService.deleteStar(user.getId(),id);
        Article article = articleService.queryArticleById(id);
        StarVo vo = new StarVo(null,article.getStarCount());

        return ResponseEntity.ok(ResultEntity.ok(vo));
    }
}
