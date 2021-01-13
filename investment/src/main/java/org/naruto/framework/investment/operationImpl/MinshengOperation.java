package org.naruto.framework.investment.operationImpl;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.naruto.framework.investment.common.KeyBordManager;
import org.naruto.framework.investment.connect.SessionManager;
import org.naruto.framework.investment.install.AppInfo;
import org.naruto.framework.investment.install.Apps;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.service.AccountOperation;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

@Scope("prototype")
@Service("minsheng")
public class MinshengOperation implements AccountOperation {

    private static final Logger log = LoggerFactory.getLogger(MinshengOperation.class);

    private  Map<String, Point> tokenInputKeyboard ;

    static AppInfo appInfo = Apps.apps.get("minsheng");

    @Autowired
    private SessionManager sessionManager;

    public void navToAccountPage(AndroidDriver<MobileElement> driver){

    }

    public void logon (AndroidDriver<MobileElement> driver,Account account) throws InterruptedException{

        WebDriverWait wait = new WebDriverWait(driver, 10);

        driver.findElementById("com.cmbc.hwydlsyh:id/bt_iFirstPageFragmentBanner_login").click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.cmbc.hwydlsyh:id/edtTxtUserName"))).sendKeys(account.getLoginId());

        driver.findElementById("com.cmbc.hwydlsyh:id/edt_password").click();

        Thread.sleep(2000);
        KeyBordManager.minsheng_tap(driver,tokenInputKeyboard,account.getLoginPwd());

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.cmbc.hwydlsyh:id/btnLogin"))).click();
    }

    @Override
    public void connect(Account account) throws Exception {
        AndroidDriver<MobileElement> driver = sessionManager.activateApp(account.getAppLocation(),account.getType());
        //激活应用
        tokenInputKeyboard = KeyBordManager.getKeyBord(driver,account.getType());
        // 等待关闭闪屏
        Thread.sleep(3000);
        try {
            this.logon(driver,account);
        } catch (Exception e) {
            log.info("已经登录了客户端");
        }
    }

    @Override
    public Account queryBalance(Account account) throws Exception {
        AndroidDriver<MobileElement> driver = sessionManager.activateApp(account.getAppLocation(),account.getType());
        this.connect(account);
        WebDriverWait wait = new WebDriverWait(driver, 5);
        //读取现金值
        String balance =  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.cmbc.hwydlsyh:id/tv_fFirstPage_allMoneyShow"))).getText();
        Float balance_ = Float.parseFloat(balance.replaceAll("港元","").replaceAll(",","").trim());
        account.setBalance(balance_);
        return account;
    }
}
