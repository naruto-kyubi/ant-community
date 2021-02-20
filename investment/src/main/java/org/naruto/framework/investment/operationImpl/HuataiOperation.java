package org.naruto.framework.investment.operationImpl;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.naruto.framework.investment.common.KeyBordManager;
import org.naruto.framework.investment.connect.SessionManager;
import org.naruto.framework.investment.install.AppInfo;
import org.naruto.framework.investment.install.Apps;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.FundTrans;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.Stock;
import org.naruto.framework.investment.service.AccountOperation;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.Map;

@Log
@Scope("prototype")
@Service("huatai_")
public class HuataiOperation extends BaseOperation {

    private  Map<String, Point> tokenInputKeyboard ;

    static AppInfo appInfo = Apps.apps.get("huatai");


    public void logon (AndroidDriver<MobileElement> driver, String pwd, String tokenPwd) throws InterruptedException{
        WebDriverWait wait = new WebDriverWait(driver, 6);


        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.lphtsccft.zlqqt2:id/login_et_password"))).sendKeys(pwd);
        driver.findElement(By.id("com.lphtsccft.zlqqt2:id/login_btn_login_account")).click();

        String expiredButton = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v7.widget.LinearLayoutCompat/android.widget.ScrollView/android.widget.LinearLayout/android.widget.Button";
        //判断切换到的token管理器是否过期，提示："超过自动锁定时间"
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(expiredButton))).click();
        } catch (Exception e) {
            log.info("no expired message!!");
        }finally {
            //token软件，跳过"不能复制一次性密码"
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("android:id/button1"))).click();
        }

        for(int i =0; i<=5; i++) {
            driver.findElement(By.id("hk.com.ayers.htai.token:id/t9_key_".concat(String.valueOf(tokenPwd.charAt(i))))).click();
        }

        driver.findElement(By.id("hk.com.ayers.htai.token:id/t9_key_ok")).click();


        String pinCode = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("hk.com.ayers.htai.token:id/pin_value"))).getText();

        driver.activateApp(appInfo.getAppPackage());

        Thread.sleep(2000);

        KeyBordManager.tap(driver,tokenInputKeyboard,pinCode);
    }


    @Override
    public void connect(Account account) throws Exception{
        AndroidDriver<MobileElement> driver = sessionManager.activateApp(account.getAppLocation(),account.getAccountType().getId());
        //激活应用
        tokenInputKeyboard = KeyBordManager.getKeyBord(driver,account.getAccountType().getId());

        WebDriverWait wait = new WebDriverWait(driver, 6);

        // 关闭广告框
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.lphtsccft.zlqqt2:id/iv_advertisement"))).click();
        } catch (Exception e) {
            log.info("no adv present!!");
        }


        try {
            //v3版本
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.lphtsccft.zlqqt2:id/main_account"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='持仓']"))).click();
        } catch (Exception e) {
            //v2版本
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,'登录')]"))).click();
        } finally {
            this.logon(driver,account.getLoginPwd(),account.getTradePwd());
        }

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,\"登录\")]")));
            this.logon(driver,account.getLoginPwd(),account.getTradePwd());
            //
        } catch (Exception e) {
            log.info("已经登录了客户端");
        }
    }

    @Override
    public Account queryBalance(Account account) throws Exception {
        AndroidDriver<MobileElement> driver = sessionManager.activateApp(account.getAppLocation(),account.getAccountType().getId());
        this.connect(account);
        WebDriverWait wait = new WebDriverWait(driver, 5);



        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.lphtsccft.zlqqt2:id/main_fortune"))).click();
       // wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,'我的资产')]"))).click();
       // wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,'现金')]"))).click();
        //读取现金值
        String balance =  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,\"港元\")]"))).getText();
        Float balance_ = Float.parseFloat(StringUtils.substringAfter(balance,"港元").replaceAll("[\b\r\n\t]*", "").replaceAll(",",""));
        account.setBalance(balance_);
        return account;
    }

    @Override
    public IPOSubscription oneCash(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        return null;
    }

    @Override
    public void logonFinanceIPO(IPOSubscription ipoSubscription) throws Exception {
        this.connect(ipoSubscription.getAccount());
    }

    @Override
    public void prepareFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        Account account = ipoSubscription.getAccount();
        AndroidDriver<MobileElement> driver = sessionManager.activateApp(account.getAppLocation(),account.getAccountType().getId());
        WebDriverWait wait = new WebDriverWait(driver, 5);
        String functionMenu = "//android.widget.FrameLayout[@resource-id='com.lphtsccft.zlqqt2:id/fragment_stub']/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View[11]/android.widget.ImageView";
        driver.findElementByXPath(functionMenu).click();
        //点击'打新股'按钮
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='打新股']"))).click();
    }

    @Override
    public IPOSubscription addFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        Account account = ipoSubscription.getAccount();
        AndroidDriver<MobileElement> driver = sessionManager.activateApp(account.getAppLocation(),account.getAccountType().getId());
        WebDriverWait wait = new WebDriverWait(driver, 5);

        String stockCode = ipoSubscription.getStock().getCode();
        while (!this.webSessionManager.shouldBeStoped(account.getAppLocation())) {
            try {
                driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).setSwipeDeadZonePercentage(0.3).setMaxSearchSwipes(2).scrollIntoView(new UiSelector().textContains(\"".concat(stockCode).concat("\").instance(0))"));

                WebElement element1 = driver.findElement(By.xpath("//*[contains(@text,'" + stockCode + "')]/../android.view.ViewGroup/android.widget.TextView"));

                String text = element1.getText();

                if (text.equals("认购")){

                    element1.click();

                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='其他股数' and @class='android.widget.TextView']"))).click();

                    DecimalFormat df = new DecimalFormat("#,###,###");

                    String shares = df.format(ipoSubscription.getPlanSubscriptionShares());
                    element1 = driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).setSwipeDeadZonePercentage(0.3).setMaxSearchSwipes(2).scrollIntoView(new UiSelector().textContains(\"".concat(shares).concat("\").instance(0))"));
                    element1.click();

                    driver.findElementByXPath("//*[@text='杠杆融资']/..").click();
                } else {
                    // 没有开始认购时间，不能导航到新股认购页面
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,'待认购')]"))).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,'可认购')]"))).click();
                    continue;
                }
            } catch (Exception e) {
                continue;
            }
            //这里进行无限循环直到手动退出
            while(!this.webSessionManager.shouldBeStoped(account.getAppLocation())) {
                try {
                    //点击申请认购'按钮；
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='申请认购']"))).click();

                    //我已经阅读并同意；
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='我已阅读并同意']"))).click();

                    //点击'提交申请'按钮；生产环境要取消掉注释;
                    //       driver.findElementByXPath("//*[@text='提交申请']").click();
                }catch(Exception e){

                }
            }
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>退出新股申购<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            return ipoSubscription;
        }
        log.info("===================================退出新股申购=============================================");
        return ipoSubscription;
    }

    @Override
    public IPOSubscription sign(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        return null;
    }

    @Override
    public FundTrans executeTrans(FundTrans fundTrans, Account account, Account BankAccount) throws Exception {
        return null;
    }

}
