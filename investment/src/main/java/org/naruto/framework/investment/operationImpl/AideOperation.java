package org.naruto.framework.investment.operationImpl;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import lombok.extern.java.Log;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.FundTrans;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.Stock;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Log
@Scope("prototype")
@Service("aide_")
public class AideOperation extends BaseOperation {

    private String getPinCode(String mobileId,String pinCodePwd) throws Exception{

        AndroidDriver pinCodeDriver = sessionManager.activateApp(mobileId,"aide_pincode");
        WebDriverWait wait = new WebDriverWait(pinCodeDriver, 5);
        try{
            for(int i =0; i<pinCodePwd.length(); i++) {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("hk.com.ayers.fsec.token1:id/t9_key_" + pinCodePwd.charAt(i)))).click();;
            }

            pinCodeDriver.findElementById("hk.com.ayers.fsec.token1:id/t9_key_ok").click();
        }catch (Exception e){
            e.printStackTrace();
            log.info("no password validator!");
        }
        String text = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("hk.com.ayers.fsec.token1:id/pin_value"))).getText();

        sessionManager.closeApp(mobileId,"aide_pincode");
        return text;
    }

    @Override
    public void connect(Account account) throws Exception {
        AndroidDriver<MobileElement> driver = webSessionManager.getConnection(account.getAppLocation());
        String logonPageURL = "https://fsec.ayers.com.hk/mts.web/Web2/login/FSEC/#big5";
        driver.get(logonPageURL);

        WebDriverWait wait = new WebDriverWait(driver, 20);

        //输入账号、密码；
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='user']"))).sendKeys(account.getAccountNo());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='password']"))).sendKeys(account.getTradePwd());

        //点击登录；
        MobileElement element1 = driver.findElementByXPath("//input[@id='btnLogin']");
        driver.executeScript("arguments[0].click();",element1);


        String pinCode =  this.getPinCode(account.getAppLocation(),account.getTradePwd());

        driver.activateApp("com.android.chrome");

        //切换到iframe；
        driver.switchTo().frame("frame1");
        //输入密码器编码；

        //输入密码器编码；
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='tbxESP']"))).sendKeys(pinCode);

        element1 = driver.findElementByXPath("//input[@id='btnSubmit_ESP']");
        driver.executeScript("arguments[0].click();",element1);

        //返回主页面；
        driver.switchTo().parentFrame();

         //点击同意按钮
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='btnCloseLoginMsg']"))).click();

        driver.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    @Override
    public Account queryBalance(Account account) throws Exception {
        return null;
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

    @Override
    public void logonFinanceIPO(IPOSubscription ipoSubscription) throws Exception {
       this.connect(ipoSubscription.getAccount());
    }

    @Override
    public void prepareFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        String shares = String.valueOf(ipoSubscription.getPlanSubscriptionShares());

        log.info("The share is ...".concat(shares));

        Account account = ipoSubscription.getAccount();
        AndroidDriver<MobileElement> driver = webSessionManager.getConnection(account.getAppLocation());


        WebDriverWait wait = new WebDriverWait(driver, 20);

        // 点击其他
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='lanmu_others_title_hdr']/a"))).click();

        //点击新股认购
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='lanmu_others_title_dtl']/li[1]/a"))).click();

        // 切换到新股认购界面
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='frame1']")));
        //切换到iframe；
        driver.switchTo().frame("frame1");

        // 查找申购的新股代码，并确认；
        String stockCode = ipoSubscription.getStock().getCode();

        String urlXpath = "//a[contains(@href,\"".concat(stockCode).concat("\")]");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(urlXpath))).click();
    }

    @Override
    public IPOSubscription addFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        Account account = ipoSubscription.getAccount();
        AndroidDriver<MobileElement> driver = webSessionManager.getConnection(account.getAppLocation());

        // 获取计划申购数量;
        String shares = String.valueOf(ipoSubscription.getPlanSubscriptionShares());


        WebDriverWait wait = new WebDriverWait(driver, 20);
        while (!this.webSessionManager.shouldBeStoped(account.getAppLocation())) {
            driver.switchTo().parentFrame();
            driver.switchTo().frame("frame1");
            try {

                // 设定申购数量
                WebElement qty =  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='qty']"))) ;

                String elementType =  qty.getTagName();

                if(elementType.equals("select")) {
                    //  设置申请股数
                    Select d1 = new Select(qty);
                    // 获取选定的值，并去掉逗号，如1,000,000
                    //   String amount = d1.getAllSelectedOptions().get(0).getText().replaceAll(",", "");

                    //    直接赋值，比对会影响速度
                    //    if (!amount.equals(shares)) {
                    d1.selectByValue(shares);
                    //    }
                    // 设置申请倍数
                    WebElement loanType =  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='loan_type']"))) ;
                    Select d2 = new Select(loanType);

                    d2.selectByValue("F");

                    //  进入循环后手动提交，不自动提交
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name='btnApply']"))).click();
                }
                else{
                    //确认一下手数，margin，等信息是否正确，然后再提交。

                    // 点击提交按钮
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name='btnApply']"))).click();
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name='btnOK']"))).click();
                    continue;
                }
            } catch (Exception e) {

                e.printStackTrace();
                continue;
            }
            // wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='CaptchaResult']"))).sendKeys("NMTP");
        }
        return ipoSubscription;
    }
}
