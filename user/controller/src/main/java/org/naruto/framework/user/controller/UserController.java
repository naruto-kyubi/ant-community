package org.naruto.framework.user.controller;


import org.modelmapper.ModelMapper;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.file.FileService;
import org.naruto.framework.core.utils.PageUtils;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.core.session.ISessionService;
import org.naruto.framework.user.domain.ThirdPartyUser;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.service.ThirdPartyUserService;
import org.naruto.framework.user.service.UserService;
import org.naruto.framework.user.vo.RegisterRequest;
import org.naruto.framework.user.vo.ResetPasswordRequest;
import org.naruto.framework.user.vo.UpdateUserRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class UserController {

    @Value("${img.location}")
    private String location;

    @Value("${spring.mvc.static-path-pattern}")
    private String staticPathPattern;
    @Autowired
    private UserService userService;

    @Autowired
    private ThirdPartyUserService thirdPartyUserService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private ISessionService sessionService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;



    @RequestMapping(value = "/v1/users/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> register(@Validated @RequestBody RegisterRequest registerRequest){
        User user =  modelMapper.map(registerRequest,User.class);
        return ResponseEntity.ok(ResultEntity.ok(userService.register(user)));
    }

    @RequestMapping(value = "/v1/users/registerCaptcha", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> getRegisterCaptcha(@NotBlank(message = "mobie number is blank") @RequestParam(name = "mobile") String mobile) {
        captchaService.createCaptcha(mobile, CaptchaType.SINGUP);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }

    @RequestMapping(value = "/v1/users/resetPassword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> resetPassword(@Validated @RequestBody ResetPasswordRequest resetPasswordRequest){
        User user =  modelMapper.map(resetPasswordRequest,User.class);
        return ResponseEntity.ok(ResultEntity.ok(userService.resetPassword(user)));
    }

    @RequestMapping(value = "/v1/users/forgotPasswordCaptcha", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> getForgotPasswordCaptcha(@NotBlank(message = "mobie number is blank") @RequestParam(name = "mobile") String mobile) {
        captchaService.createCaptcha(mobile, CaptchaType.FORGOTPASSWORD);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/users/currentUser", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> getCurrentUser(
            HttpServletRequest request, HttpServletResponse response) {
        User sessionUser = (User) sessionService.getCurrentUser(request);
        User localUser = userService.queryUserById(sessionUser.getId());
        return ResponseEntity.ok(ResultEntity.ok(localUser));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/users/avatar", method = RequestMethod.POST)
    public ResponseEntity<ResultEntity> setAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String userId = ((User)sessionService.getCurrentUser(request)).getId();
        String imageUrl = fileService.uploadFile(file);
        return ResponseEntity.ok(ResultEntity.ok(userService.setAvatar(imageUrl,userId)));
    }

    //多条件组合查询查询
    @ResponseBody
    @RequestMapping(value = "/v1/users", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> query(
            @RequestParam(required = false) Map map,
            HttpServletRequest request, HttpServletResponse response) {

        //查询条件参数验证。
        Page page = userService.queryPage(map);
        return ResponseEntity.ok(ResultEntity.ok(page.getContent(), PageUtils.wrapperPagination(page)));
    }

    // 新增记录；
    @ResponseBody
    @RequestMapping(value = "/v1/users", method = RequestMethod.POST)
    public ResponseEntity<ResultEntity> create(
            @Validated @RequestBody User user,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(ResultEntity.ok(userService.save(user)));
    }

    // 修改记录；
    @ResponseBody
    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ResultEntity> update(
            @PathVariable("id") String id,
            @Validated @RequestBody UpdateUserRequest updateUserRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (null==updateUserRequest) return null;
        User dbUser = userService.queryUserById(id);

        modelMapper.map(updateUserRequest,dbUser);
        return ResponseEntity.ok(ResultEntity.ok(userService.save(dbUser)));
    }

    // 删除记录；
    @ResponseBody
    @RequestMapping(value = "/v1/users/{ids}", method = RequestMethod.DELETE)
    public ResponseEntity<ResultEntity> delete(
            @PathVariable("ids") String ids,
            HttpServletRequest request,
            HttpServletResponse response) {

        String[] _ids = ids.split(",");
        List idList = Arrays.asList(_ids);
        if (!CollectionUtils.isEmpty(idList)) userService.delete(idList);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }


    // bind with third party；
    @ResponseBody
    @RequestMapping(value = "/v1/users/bind", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> bind(
            @RequestParam("authType") String authType,
            @RequestParam("authCode") String authCode,
            HttpServletRequest request,
            HttpServletResponse response) {

        User sessionUser = (User) sessionService.getCurrentUser(request);
        ThirdPartyUser thirdPartyUser = thirdPartyUserService.bind(sessionUser,authType,authCode);
        return ResponseEntity.ok(ResultEntity.ok(thirdPartyUser));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/users/unbind", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> unbind(
            @RequestParam("authType") String authType,
            HttpServletRequest request,
            HttpServletResponse response) {

        User sessionUser = (User) sessionService.getCurrentUser(request);
        thirdPartyUserService.unbind(sessionUser,authType);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/users/queryBinds", method = RequestMethod.GET)
    public ResponseEntity<ResultEntity> queryBinds(HttpServletRequest request, HttpServletResponse response) {

        User sessionUser = (User) sessionService.getCurrentUser(request);
        List<ThirdPartyUser> thirdPartyUserList = thirdPartyUserService.queryThirdPartyUsersByUser(sessionUser);
        return ResponseEntity.ok(ResultEntity.ok(thirdPartyUserList));
    }

    @ResponseBody
    @RequestMapping(value = "/v1/users/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public ResponseEntity<ResultEntity> queryById(@PathVariable("id") String id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(ResultEntity.ok(user));
    }
}