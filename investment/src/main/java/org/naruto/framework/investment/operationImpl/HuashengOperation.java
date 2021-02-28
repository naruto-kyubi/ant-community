package org.naruto.framework.investment.operationImpl;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.touch.offset.PointOption;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.naruto.framework.investment.connect.SessionManager;
import org.naruto.framework.investment.install.AppInfo;
import org.naruto.framework.investment.install.Apps;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.FundTrans;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.Stock;
import org.naruto.framework.investment.service.AccountOperation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;


@Log
@Scope("prototype")
@Service("huasheng_")
public class HuashengOperation extends BaseOperation{
    static AppInfo appInfo = Apps.apps.get("huasheng");
    @Autowired
    private SessionManager sessionManager;
    private boolean isAdvClosed = false;


    public void closeAdv(AndroidDriver<MobileElement> driver){
        if(isAdvClosed) return;
        WebDriverWait wait = new WebDriverWait(driver, 2);

        //关闭广告1
        try {
            log.info("navToAccountPage1-------"+ driver.currentActivity());
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.huasheng.stock:id/button_cancel"))).click();

        } catch (Exception e) {
            log.info("no adv present!!");
        }
        //关闭广告2
        try {
            log.info("navToAccountPage2-------"+ driver.currentActivity());
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.huasheng.stock:id/btn_close"))).click();

        } catch (Exception e) {
            log.info("no adv present!!");
        }

         isAdvClosed = true;
    }

    public void logon (AndroidDriver<MobileElement> driver,String tradePwd) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 3);

        this.closeAdv(driver);
        //手机密码登录
        log.info("logon2-------"+ driver.currentActivity());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@content-desc=\"交易\"]"))).click();

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[3]/android.widget.EditText"))).sendKeys(tradePwd);
            driver.findElement(By.id("com.huasheng.stock:id/btn_login")).click();
        } catch (Exception e) {
            log.info("已经登录了客户端");
        }
    }

    public void buyNewStock(AndroidDriver<MobileElement> driver,String stockNumber,String tradePwd) throws InterruptedException {

        //五秒中定位不到控件，就抛出异常
        WebDriverWait wait = new WebDriverWait(driver, 4);
        boolean isContinue = true;

        while (isContinue) {
            try{
                switch (driver.currentActivity()){
                    //交易页面
                    case ".ui.MainActivity":
                        //.ui.MainActivity
//                        log.info("1--------------" + driver.currentActivity());
//                        this.closeAdv(driver);
//
//                        while(1==1) {
//                            //点击底部'交易'按钮
//                            try {
//                                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@content-desc=\"交易\"]"))).click();
//                                //点击'港股'tabitem
//                                driver.findElementsById("com.huasheng.stock:id/tv_tabItem").get(0).click();
//                                //.ui.MainActivity
//                                log.info("2--------------" + driver.currentActivity());
//                                //新股认购
//                                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.huasheng.stock:id/ipo"))).click();
//                                //    WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.huasheng.stock:id/ipo")));
//                                break;
//                            } catch (Exception e) {
//                                try {
//                                    this.logon(driver, tradePwd);
//                                } finally {
//                                    continue;
//                                }
//                            }
//                        }
                        //新股认购列表界面
                    case ".ui.ipo.IPOStocksActivity":
                        log.info("3--------------" + driver.currentActivity());
                        //选择'新股'

                        boolean isFindStock = false;
                        int scrollStep = 0;
                        WebElement element = null;
                        while (!isFindStock) {
                            if(scrollStep < 2) {
                                try {
                                    element = driver.findElement(By.xpath("//*[contains(@text,'" + stockNumber + "')]/../../../../android.widget.LinearLayout[4]/android.widget.TextView"));
                                    isFindStock = true;
                                    element.click();
                                } catch (Exception e) {
                                    (driver).
                                            findElementByAndroidUIAutomator
                                                    ("new UiScrollable(new UiSelector().resourceId(\"com.huasheng.stock:id/sticky_recycler_view\")).setAsVerticalList().setSwipeDeadZonePercentage(0.1).setMaxSearchSwipes(50).scrollForward(20)");
                                    scrollStep += 1;
                                    log.info("current scroll step is " + String.valueOf(scrollStep));
                                    continue;
                                }
                            }else{
                                String back = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout[1]/android.widget.TextView";
                                try {
                                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(back))).click();
                                } finally {
                                    break;
                                }
                            }
                        }
//                        continue;

                        //新股认购界面
                    case ".ui.webview.BridgeWebViewUI":
                        String back = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.ImageView";

                        WebElement titleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.huasheng.stock:id/titleTV")));
                        String title = titleElement.getAttribute("text");
                        switch (title){
                            case "新股认购":
                                //-----------'新股认购'界面；
                                log.info("4--------------" + driver.currentActivity());
//                                WebElement rzrgElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='融资认购']")));
//                                if(rzrgElement.getAttribute("clickable").equals("false")) {
//                                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(back))).click();
//                                    continue;
//                                }
                                //选择'融资认购'
//                                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='融资认购']"))).click();
                                AndroidTouchAction action = new AndroidTouchAction(driver);
                                action.press(PointOption.point(900,680)).release().perform();
                                //点击'认购'------
//                                driver.findElement(By.xpath("//*[@text='认购']")).click();
                                AndroidTouchAction action1 = new AndroidTouchAction(driver);
                                action1.press(PointOption.point(890,2090)).release().perform();
                                Thread.sleep(500);
                                break;
                            case "订购确认":
                                //----------'订购确认'界面；
                                log.info("5--------------" + driver.currentActivity());
                                //点击 "确认认购协议"
                                String aggrenStatement = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/com.tencent.tbs.core.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View[2]/android.view.View[1]/android.view.View/android.view.View[1]";
                                try{
                                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(aggrenStatement))).click();
                                }catch(Exception e){
//                                    e.printStackTrace();
                                    driver.findElementByXPath(back).click();
                                    break;
                                }

                                while(true){
                                    try {
                                        log.info("6--------------" + driver.currentActivity());
                                        //点击“确认提交”
                                        driver.findElement(By.xpath("//*[contains(@text,'确认提交')]")).click();

                                        log.info("7--------------" + driver.currentActivity());
                                        //输入交易密码
                                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/com.tencent.tbs.core.webkit.WebView/android.webkit.WebView/android.view.View[2]/android.view.View/android.view.View/android.widget.ListView/android.view.View[2]/android.widget.EditText"))).sendKeys(tradePwd);
                                        //确认
                                        driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/com.tencent.tbs.core.webkit.WebView/android.webkit.WebView/android.view.View[2]/android.view.View/android.view.View/android.view.View/android.widget.Button[2]").click();
                                    }catch(Exception e){
//                                        e.printStackTrace();
                                    }
                                }
                        }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connect(Account account) throws Exception {
        AndroidDriver<MobileElement> driver = sessionManager.activateApp(account.getAppLocation(),account.getAccountType().getId());
        //激活应用
        this.logon(driver,account.getTradePwd());
    }

    @Override
    public Account queryBalance(Account account) throws Exception {
        this.connect(account);
        AndroidDriver<MobileElement> driver = sessionManager.activateApp(account.getAppLocation(),account.getAccountType().getId());
        WebDriverWait wait = new WebDriverWait(driver, 3);
        //
        String balance =  wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.huasheng.stock:id/totalAsserts"))).getText();
        Float balance_ = Float.parseFloat(StringUtils.substringBefore(balance,"累计").replaceAll("[\b\r\n\t]*", "").replaceAll(",",""));
        account.setBalance(balance_);
        return account;
    }

    @Override
    public IPOSubscription oneCash(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        return null;
    }

    @Override
    public void logonFinanceIPO(IPOSubscription ipoSubscription) throws Exception {
        Account account = ipoSubscription.getAccount();

        AndroidDriver<MobileElement> driver = webSessionManager.getConnection(account.getAppLocation());
        WebDriverWait wait = new WebDriverWait(driver, 20);

        // 到登录界面
        String logonPageURL = "https://www.vbkr.com/passport/login";
        driver.get(logonPageURL);
        // 手机密码登录
        String urlXpath = "//a[contains(text(),\"密码登录\")]";
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(urlXpath))).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='mobile']"))).sendKeys(account.getLoginId());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='password']"))).sendKeys(account.getLoginPwd());

       // checkbox,不能直接点击，元素被覆盖，需要通过执行脚本进行点击；
        WebElement element1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='app']/section/div[2]/input")));

        driver.executeScript("arguments[0].click();",element1);

        element1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='app']/a")));
        driver.executeScript("arguments[0].click();",element1);

    }

    @Override
    public void prepareFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        Account account = ipoSubscription.getAccount();

        AndroidDriver<MobileElement> driver = webSessionManager.getConnection(account.getAppLocation());
        WebDriverWait wait = new WebDriverWait(driver, 20);

        // 直接到新股认购界面
        String subscriptionPageURL = "https://www.vbkr.com/ipo/hk/v2/ipo-hk-index";
        driver.get(subscriptionPageURL);


        // 查找申购的新股代码，并确认；
        String stockCode = ipoSubscription.getStock().getCode();

        String urlXpath = "//td[contains(text(),\"".concat(stockCode).concat("\")]/following-sibling::td/following-sibling::td/span");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(urlXpath))).click();


        // 输入交易密码并登录
       // wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='app']/a"))).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='tradeLoginDiv']/div/div/ul/li[2]/div/following-sibling::input"))).sendKeys(account.getTradePwd());

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='tradeLoginDiv']/div/div/footer/button"))).click();



    }

    @Override
    public IPOSubscription addFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {

        Account account = ipoSubscription.getAccount();

        AndroidDriver<MobileElement> driver = webSessionManager.getConnection(account.getAppLocation());
        WebDriverWait wait = new WebDriverWait(driver, 10);
        // 融资认购
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//article[@id='app']/section/div[3]/div/div/ul/li[2]"))).click();

        // 选择手数
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//article[@id='app']/section/div[3]/div/div[4]/div/a"))).click();

        DecimalFormat df = new DecimalFormat("#,###,###");

        String shares = df.format(ipoSubscription.getPlanSubscriptionShares());

        String urlXpath = "//span[@class='num']/em[text()='".concat(shares).concat("']");

       // driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).setSwipeDeadZonePercentage(0.3).setMaxSearchSwipes(2).scrollIntoView(new UiSelector().textContains(\"".concat(shares).concat("\").instance(0))"));


        // 滑动到第一项
        WebElement element1 =  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(urlXpath)));

        driver.executeScript("arguments[0].scrollIntoView(true);",element1);
        driver.executeScript("arguments[0].click();",element1);
        // 点击认购
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='fixed-bt-con']/div/a[2]"))).click();

        // 认购协议
        element1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='checkbox']")));
        driver.executeScript("arguments[0].click();",element1);

        // 确认提交
        element1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'确认提交')]")));
        driver.executeScript("arguments[0].click();",element1);

        // 二次确认
        element1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='p-bottom']/a[2]")));
        driver.executeScript("arguments[0].click();",element1);

        return null;
    }

    @Override
    public IPOSubscription cancelFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {
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
