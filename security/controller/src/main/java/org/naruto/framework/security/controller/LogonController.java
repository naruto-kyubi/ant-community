package org.naruto.framework.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.security.service.ILogonService;
import org.naruto.framework.user.service.IOauthService;
import org.naruto.framework.core.session.ISessionService;
import org.naruto.framework.security.service.LogonUser;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class LogonController {

    @Autowired
    private ILogonService logonService;

    @Autowired
    private ISessionService sessionService;

    @Autowired
    private CaptchaService captchaService;


    /**
     * 用户身份认证（支持用户名密码、短信验证码、微博）
     * @param logonUser
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/v1/logon/account", method = RequestMethod.POST ,produces ="application/json")
    public ResponseEntity<ResultEntity> logon(@Validated @RequestBody LogonUser logonUser, HttpServletRequest request, HttpServletResponse response) {

        User user = (User) logonService.authenticate(logonUser);
        //if exists thrid party user info. bind it(weixin/weibo/taobo etc);
        if(StringUtils.isNotEmpty(logonUser.getBindType())
                && StringUtils.isNotEmpty(logonUser.getBindUid())
                && !logonUser.getAuthType().equals(logonUser.getBindType())
                ){
            IOauthService oauthService = logonService.getOAuthService(logonUser.getBindType());
            oauthService.bind(user,logonUser.getBindType(),logonUser.getBindUid(),logonUser.getBindName());
        }
        return ResponseEntity.ok(ResultEntity.ok(user));
    }

    /**
     * 登录用户注销
     * @param request
     * @return
     */
    @RequestMapping(value = "/v1/logon/logout", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> logout(HttpServletRequest request) {

        User user = (User) sessionService.getCurrentUser(request);
        sessionService.logout(user);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }

    /**
     * 获取登录captcha。
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/v1/logon/captcha", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> getCaptcha(@Validated @RequestParam(name = "mobile") String mobile) {
        captchaService.createCaptcha(mobile, CaptchaType.LOGON);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }
}
