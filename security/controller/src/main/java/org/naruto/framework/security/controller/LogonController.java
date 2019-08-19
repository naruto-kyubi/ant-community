package org.naruto.framework.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.security.LogonUser;
import org.naruto.framework.core.security.SessionUtils;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.security.service.LogonService;
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
    LogonService logonService;

    @Autowired
    SessionUtils sessionUtils;

    @Autowired
    CaptchaService captchaService;

    @ResponseBody
    @RequestMapping(value = "/v1/logon/account", method = RequestMethod.POST ,produces ="application/json")
    public ResponseEntity<ResultEntity> logon(@Validated @RequestBody LogonUser logonUser, HttpServletRequest request, HttpServletResponse response) {

        User user = logonService.authenticate(logonUser);
        //if exists thrid party user info. bind it(weixin/weibo/taobo etc);
        if(StringUtils.isNotEmpty(logonUser.getBindType())
                && StringUtils.isNotEmpty(logonUser.getBindUid())
                && !logonUser.getAuthType().equals(logonUser.getBindType())
                ){
            logonService.bind(user,logonUser.getBindType(),logonUser.getBindUid(),logonUser.getBindName());
        }
        return ResponseEntity.ok(ResultEntity.ok(user));
    }

    @RequestMapping(value = "/v1/logon/logout", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> logout(HttpServletRequest request) {

        User user = sessionUtils.getCurrentUser(request);
        sessionUtils.logout(user);

//        logonService.logout(null);
//        //在这里执行退出系统前需要清空的数据
//        Subject subject= SecurityUtils.getSubject();
////        ServletContext context= request.getServletContext();
//        try {
//            subject.logout();
////            context.removeAttribute("error");
//        }catch (SessionException e){
//            log.error(e.getMessage());
//            e.printStackTrace();
//        }
        return ResponseEntity.ok(ResultEntity.ok(null));
    }

    @RequestMapping(value = "/v1/logon/captcha", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> getCaptcha(@Validated @RequestParam(name = "mobile") String mobile) {
        captchaService.createCaptcha(mobile, CaptchaType.LOGON);
        return ResponseEntity.ok(ResultEntity.ok(null));
    }

}
