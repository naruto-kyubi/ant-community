package org.naruto.framework.article.controller;

import org.naruto.framework.article.domain.Tag;
import org.naruto.framework.article.domain.UserTag;
import org.naruto.framework.article.service.TagService;
import org.naruto.framework.article.vo.TagVo;
import org.naruto.framework.core.security.SessionUtils;
import org.naruto.framework.core.utils.ObjUtils;
import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private SessionUtils sessionUtils;

    @ResponseBody
    @RequestMapping(value = "/v1/tags/query", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> query(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        Page page = tagService.queryByPage(map);

        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/user/tags/subscribed", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryUserTags(
                @RequestParam(required = false) Integer currentPage,
                @RequestParam(required = false) Integer pageSize,
                HttpServletRequest request,
                HttpServletResponse response
            ) {
        Map map = new HashMap();
        map.put("currentPage",currentPage);
        map.put("pageSize",pageSize);

        User user = sessionUtils.getCurrentUser(request);
        Page<Tag> page = tagService.queryUserTags(user.getId(),map);
        List<TagVo> voList = page.getContent().stream().map(item->{
            TagVo vo = (TagVo) ObjUtils.convert(item, TagVo.class);
            vo.setUserId(user.getId());
            return vo;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(ResultEntity.ok(voList, PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/tags/all", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryAllTags(
            @RequestParam(required = false) Integer currentPage,
            @RequestParam(required = false) Integer pageSize,
            HttpServletRequest request
    ) {
        Map map = new HashMap();
        map.put("currentPage",currentPage);
        map.put("pageSize",pageSize);

        User user = sessionUtils.getCurrentUser(request);
        Page page = tagService.queryTags(user.getId(),map);

        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/tag/add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> addComment(@Validated @RequestBody Tag tag, HttpServletRequest request){

        return ResponseEntity.ok(ResultEntity.ok(tagService.saveTag(tag)));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/articles/tags", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryTags(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        return ResponseEntity.ok(ResultEntity.ok(tagService.queryTags()));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/user/tag/subscribe", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> addUserTag(@Validated @RequestBody UserTag userTag, HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        userTag.setUserId(user.getId());
        UserTag ut = tagService.save(userTag);

        return ResponseEntity.ok(ResultEntity.ok(ut));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/user/tag/{tagId}/unsubscribe", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> deleteUserTags(@PathVariable("tagId") String tagId, HttpServletRequest request){

        User user = sessionUtils.getCurrentUser(request);
        tagService.deleteUserTags(user.getId(),tagId);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }
}
