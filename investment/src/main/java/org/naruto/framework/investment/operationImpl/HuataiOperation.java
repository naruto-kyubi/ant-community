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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Map;

@Log
@Scope("prototype")
@Service("huatai")
public class HuataiOperation implements AccountOperation {

    private  Map<String, Point> tokenInputKeyboard ;

    static AppInfo appInfo = Apps.apps.get("huatai");

    @Autowired
    private SessionManager sessionManager;

//    public String buy(HuataiIpoRequest huataiIpoRequest) throws MalformedURLException, InterruptedException {
//        AndroidDriver<MobileElement> driver = sessionManager.activateApp(huataiIpoRequest.getMobileId(),"huatai");
//        //激活应用
//        tokenInputKeyboard = KeyBordManager.getKeyBord(driver,"huatai");
//
//        // 等待关闭闪屏
//        Thread.sleep(3000);
//        WebDriverWait wait = new WebDriverWait(driver, 10);
//        this.navToAccountPage(driver);
//        try {
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,\"登录\")]")));
//            this.logon(driver,huataiIpoRequest.getPwd(),huataiIpoRequest.getTokenpwd());
//        } catch (Exception e) {
//            log.info("已经登录了客户端");
//        }
//        this.buyNewStock(driver,new Point(huataiIpoRequest.getStockPointX(),huataiIpoRequest.getStockPointY()),huataiIpoRequest.getStockShare(),huataiIpoRequest.getSelectedStock(),huataiIpoRequest.getStockNumber());
//        return null;
//    }
    public void logon (AndroidDriver<MobileElement> driver, String pwd, String tokenPwd) throws InterruptedException{
        WebDriverWait wait = new WebDriverWait(driver, 6);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,'登录')]"))).click();

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

    public void buyNewStock(AndroidDriver<MobileElement> driver, Point newStockPoint, String stockShare, int selectedStock, String stockNumber) throws InterruptedException{

        WebDriverWait wait = new WebDriverWait(driver, 5);
        String currentActivity = "";
        while(true){

            try{
                switch (driver.currentActivity()){

                    case "com.htsc.zlgapp.main.MainActivity":
                        currentActivity = "com.htsc.zlgapp.main.MainActivity";

                        String functionMenu = "//android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.widget.ImageView[2]";
                        driver.findElementByXPath(functionMenu).click();
                        //点击'打新股'按钮
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='打新股']"))).click();
                        log.info("1-------------" + driver.currentActivity());

                    case "com.lphtsccft.zhangle.rn.HtscReactNativeActivity":
//                      if("com.lphtsccft.zhangle.rn.HtscReactNativeActivity".equals(currentActivity)){
//                           //说明之前进入过该窗口；回退到主窗口
//                            driver.navigate().back();
//                            break;
//                       }
                        currentActivity = "com.lphtsccft.zhangle.rn.HtscReactNativeActivity";

                      //  WebElement we = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='android.widget.TextView' and @text='" + stockNumber + "']/..")));
                       //  driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).setSwipeDeadZonePercentage(0.3).setMaxSearchSwipes(2).scrollIntoView(new UiSelector().textContains(\"".concat(stockNumber).concat("\").instance(0))")).click();
                       // we.findElement(By.xpath("//*[@text='认购']")).click();;

                        driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).setSwipeDeadZonePercentage(0.3).setMaxSearchSwipes(2).scrollIntoView(new UiSelector().textContains(\"".concat(stockNumber).concat("\").instance(0))"));

                        // 第一次滚动不能准确定位当前标的的位置，因此滚动完成后再使用定位进行点击
                        //driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).setSwipeDeadZonePercentage(0.3).setMaxSearchSwipes(2).scrollIntoView(new UiSelector().textContains(\"".concat(stockNumber).concat("\").instance(0))")).click();
                        //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='申请认购' and @class='android.widget.TextView']"))).click();

                        driver.findElement(By.xpath("//*[contains(@text,'" + stockNumber + "')]/../android.view.ViewGroup[2]")).click();

                        //如果获取不到"其它股数"UI元素，程序报错，将会导航到程序"主界面"；
                        log.info("2----------------" + driver.currentActivity());
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='其他股数' and @class='android.widget.TextView']"))).click();

                        log.info("3-------------" + driver.currentActivity());
                        driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).setSwipeDeadZonePercentage(0.3).setMaxSearchSwipes(2).scrollIntoView(new UiSelector().textContains(\"".concat(stockShare).concat("\").instance(0))"));


                        driver.findElementByAndroidUIAutomator("new UiSelector().textContains(\"".concat(stockShare).concat("\")")).click();

                        //选择'杠杆融资',
                        // 需要验证：程序是否会自动选择'杠杆融资'
                        driver.findElementByXPath("//*[@text='杠杆融资']/..").click();

                        //

                        //这里进行无限循环直到手动退出
                        while(true) {
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
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
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

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.lphtsccft.zlqqt2:id/main_account"))).click();

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
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,'我的资产')]"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,'现金')]"))).click();
        //读取现金值
        String balance =  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,\"累计\")]"))).getText();
        Float balance_ = Float.parseFloat(StringUtils.substringBefore(balance,"累计").replaceAll("[\b\r\n\t]*", "").replaceAll(",",""));
        account.setBalance(balance_);
        return account;
    }

    @Override
    public IPOSubscription oneCash(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        return null;
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
