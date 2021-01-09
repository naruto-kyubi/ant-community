package org.naruto.framework.investment.install;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.naruto.framework.investment.connect.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
public class InstallApp {
    private static final Logger log = LoggerFactory.getLogger(InstallApp.class);
    @Autowired
    private SessionManager sessionManager;

    @RequestMapping(value = "/v1/install", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public  Boolean install(@Validated @RequestBody InstallRequest installRequest) throws MalformedURLException {
        AppInfo appInfo = Apps.apps.get(installRequest.getAppName());
        AndroidDriver<MobileElement> driver = sessionManager.getConnection(installRequest.getMobileId());
        driver.installApp("D:\\stock\\apk\\".concat(appInfo.getApkName()));
        return true;
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
