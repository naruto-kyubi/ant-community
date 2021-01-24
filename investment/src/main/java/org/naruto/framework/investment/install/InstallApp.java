package org.naruto.framework.investment.install;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.investment.connect.SessionManager;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

@RestController
public class InstallApp {
    private static final Logger log = LoggerFactory.getLogger(InstallApp.class);
    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/v1/install", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public  Boolean install(@Validated @RequestBody InstallRequest installRequest) throws MalformedURLException {
        AppInfo appInfo = Apps.apps.get(installRequest.getAppName());
        AndroidDriver<MobileElement> driver = sessionManager.getConnection(installRequest.getMobileId());
        driver.installApp("D:\\stock\\apk\\".concat(appInfo.getApkName()));
        return true;
    }

    @RequestMapping(value = "/v1/installApp", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> connect(
            @RequestParam(required = false) String account,
            HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, InterruptedException {

        Account account_ = accountRepository.queryAccountById(account);

        AppInfo appInfo = Apps.apps.get(account_.getAccountType().getId());
        AndroidDriver<MobileElement> driver = sessionManager.getConnection(account_.getAppLocation());
        if(appInfo.getAppURL().length()>1){
            driver.installApp(appInfo.getAppURL());
        }else {
            driver.installApp("D:\\stock\\apk\\".concat(appInfo.getApkName()));
        }
        return ResponseEntity.ok(ResultEntity.ok(account_));
    }

    @RequestMapping(value = "/v1/installAll", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public  Boolean installAll(@Validated @RequestBody InstallRequest installRequest) throws MalformedURLException {
       //
        AndroidDriver<MobileElement> driver = sessionManager.getConnection(installRequest.getMobileId());
        for (String key : Apps.apps.keySet()){
            AppInfo appInfo = Apps.apps.get(key);
            driver.installApp("D:\\stock\\apk\\".concat(appInfo.getApkName()));
        }
        return true;
    }
}
