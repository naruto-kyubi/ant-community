package org.naruto.framework.investment.operationImpl;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.naruto.framework.investment.connect.SessionManager;
import org.naruto.framework.investment.install.AppInfo;
import org.naruto.framework.investment.install.Apps;
import org.naruto.framework.investment.repository.Account;
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


@Scope("prototype")
@Service("huasheng1")
public class HuashengOperation implements AccountOperation {
    static AppInfo appInfo = Apps.apps.get("huasheng");
    @Autowired
    private SessionManager sessionManager;
    private boolean isAdvClosed = false;

    private static final Logger log = LoggerFactory.getLogger(HuashengOperation.class);

//    public String buy(HuashengIpoRequest huashengIpoRequest) throws MalformedURLException, InterruptedException {
//
//        AndroidDriver<MobileElement> driver = sessionManager.activateApp(huashengIpoRequest.getMobileId(),"huasheng");
//        this.buyNewStock(driver,huashengIpoRequest.getStockNumber(),huashengIpoRequest.getTradePwd());
//        return null;
//    }

    public void closeAdv(AndroidDriver<MobileElement> driver){
        if(isAdvClosed) return;
        WebDriverWait wait = new WebDriverWait(driver, 2);

        //关闭广告1
        try {
//            driver.findElementById("com.huasheng.stock:id/button_cancel").click();
            log.info("navToAccountPage1-------"+ driver.currentActivity());
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.huasheng.stock:id/button_cancel"))).click();

        } catch (Exception e) {
            log.info("no adv present!!");
        }
        //关闭广告2
        try {
            log.info("navToAccountPage2-------"+ driver.currentActivity());
//            driver.findElementById("com.huasheng.stock:id/btn_close").click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.huasheng.stock:id/btn_close"))).click();

        } catch (Exception e) {
            log.info("no adv present!!");
        }

         isAdvClosed = true;
    }

    public void logon (AndroidDriver<MobileElement> driver,String tradePwd) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 3);
        //手机密码登录
        log.info("logon2-------"+ driver.currentActivity());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@content-desc=\"交易\"]"))).click();

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[3]/android.widget.EditText"))).sendKeys(tradePwd);
            driver.findElement(By.id("com.huasheng.stock:id/btn_login")).click();
        } catch (Exception e) {
            e.printStackTrace();
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
        AndroidDriver<MobileElement> driver = sessionManager.activateApp(account.getAppLocation(),account.getType());
        //激活应用
        this.logon(driver,account.getTradePwd());
    }

    @Override
    public Account queryBalance(Account account) throws Exception {
        return null;
    }
}
