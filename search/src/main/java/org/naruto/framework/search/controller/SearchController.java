package org.naruto.framework.search.controller;

import org.naruto.framework.article.vo.ArticleVo;
import org.naruto.framework.core.security.SessionUtils;
import org.naruto.framework.core.utils.ObjUtils;
import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.search.article.domain.EsArticle;
import org.naruto.framework.search.article.service.ArticleEsService;
import org.naruto.framework.search.search.service.SearchEsService;
import org.naruto.framework.search.user.service.UserEsService;
import org.naruto.framework.user.domain.Follow;
import org.naruto.framework.user.domain.Mutual;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.FollowService;
import org.naruto.framework.user.service.UserService;
import org.naruto.framework.user.vo.FollowUserVo;
import org.naruto.framework.user.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SearchController {

    @Autowired
    private UserEsService userEsService;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Autowired
    private ArticleEsService articleEsService;

    @Autowired
    private SearchEsService searchEsService;

    @Autowired
    private SessionUtils sessionUtils;


    //综合查询；
    @ResponseBody
    @RequestMapping(value = "/v1/search/searchall", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> searchAll(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        Page<Map> page = searchEsService.searchMutiIndices(map);
//        final String currentUserId = sessionUtils.getCurrentUser(request).getId();
        final User currentUser = sessionUtils.getCurrentUser(request);

        List<Object> list = page.getContent().stream().map(item->{
            String type = (String) item.get("_type");

            if("article".equals(type)){
                String articleId = (String) item.get("id");
                String userId = (String) item.get("userId");
                User user = userService.queryUserById(userId);
                ArticleVo vo = (ArticleVo) ObjUtils.copyMap2Obj(item,ArticleVo.class);
                vo.setOwner(user);
                return vo;
            }else if("user".equals(type)){
                String userId = (String) item.get("id");
                Follow f = null;
                if(null!=currentUser){
                    f = followService.queryFollowByUserIdAndFollowUserId(currentUser.getId(),userId);
                }

                if(null==f){
                    item.put("mutual",Mutual.NONE.getValue());
                }else{
                    item.put("mutual",f.getMutual());
                }
                FollowUserVo followUserVo = new FollowUserVo();
                ObjUtils.copyMap2Obj(item,followUserVo);
                return followUserVo;
            }
            return item;
        }).collect(Collectors.toList());;

        return ResponseEntity.ok(ResultEntity.ok(list, PageUtils.wrapperPagination(page)));
    }

    //文章查询;
    @ResponseBody
    @RequestMapping(value = "/v1/articles/search", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> searchArticles(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

//        Page page = articleService.search(map);
        Page page = articleEsService.search(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    //用户查询；
    @ResponseBody
    @RequestMapping(value = "/v1/follows/search", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> searchUsers(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        //query search users;
        Page<UserVo> page = userEsService.search(map);
        User user =sessionUtils.getCurrentUser(request);
        List<UserVo> list = page.getContent();

        List<FollowUserVo> followList = list.stream().map(item ->{
            Follow f = null;
            if(null!=user){
                f = followService.queryFollowByUserIdAndFollowUserId(user.getId(),item.getId());
            }
            Follow follow = null;
            if(null!=f) follow =(Follow) ObjUtils.convert(f,Follow.class);
            FollowUserVo followUserVo = new FollowUserVo();

            ObjUtils.copyProperties(item,followUserVo);
            if(null==follow){
//                User u = userService.queryUserById(item.getId());
                followUserVo.setMutual(Mutual.NONE.getValue());
            }else{
//                User u = follow.getFollowUser();
                followUserVo.setMutual(follow.getMutual());
            }
            return followUserVo;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(ResultEntity.ok(followList, PageUtils.wrapperPagination(page)));
    }
    //用户查询；
    @ResponseBody
    @RequestMapping(value = "/v1/search/likethis", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> searchLikeThis(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        Page<EsArticle> page = searchEsService.searchLikeThis(map);

        List<ArticleVo> voList =  page.getContent().stream().map(item->{
            User user = userService.queryUserById(item.getUserId());
            ArticleVo vo = (ArticleVo) ObjUtils.convert(item,ArticleVo.class);
            vo.setOwner(user);
            return vo;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(ResultEntity.ok(voList, PageUtils.wrapperPagination(page)));
    }

}
