package org.naruto.framework.user.controller;

import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.core.session.ISessionService;
import org.naruto.framework.user.domain.Follow;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.FollowSearchRequest;
import org.naruto.framework.user.service.FollowService;
import org.naruto.framework.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;


    @Autowired
    private ISessionService sessionService;

//    用户之间关注（one - to -one）
    @ResponseBody
    @RequestMapping(value = "/v1/users/follow/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> query(@PathVariable("id") String id) {

        User sessionUser = (User) sessionService.getCurrentUser(null);
        Follow follow = null;
        if(null!=sessionUser){
            follow = followService.query(sessionUser.getId(),id);
        }
        return ResponseEntity.ok(ResultEntity.ok(follow));
    }

    //关注了
    @ResponseBody
    @RequestMapping(value = "/v1/users/follows", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> queryUsers(
            FollowSearchRequest searchRequest,
            HttpServletRequest request, HttpServletResponse response) {

        User user = (User) sessionService.getCurrentUser(request);
        searchRequest.setCurrentUserId(user.getId());
        Page page = followService.queryFollowUsers(searchRequest);

        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

//    新增关注
    @ResponseBody
    @RequestMapping(value = "/v1/users/follows", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> add(
            @Validated @RequestBody Follow follow,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {
        User user = (User) sessionService.getCurrentUser(request);

        userService.increaseFollowCount(user.getId(),1L);
        userService.increaseFanCount(follow.getFollowUser().getId(),1L);

        follow.setUser(user);

        return ResponseEntity.ok(ResultEntity.ok(followService.save(follow)));
    }

    // 取消关注
    @ResponseBody
    @RequestMapping(value = "/v1/users/follows/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResultEntity> delete(
            @PathVariable("id") String id,
            HttpServletRequest request,
            HttpServletResponse response) {

        User sessionUser = (User) sessionService.getCurrentUser(null);

        userService.increaseFollowCount(sessionUser.getId(),-1L);
        userService.increaseFanCount(id,-1L);

        followService.delete(sessionUser.getId(),id);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }



    //用户粉丝
    @ResponseBody
    @RequestMapping(value = "/v1/users/fans", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryFans(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        User user = (User) sessionService.getCurrentUser(request);
        map.put("currentUserId",user.getId());

        Page page = followService.queryFans(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }
}
