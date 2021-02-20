package org.naruto.framework.investment.operationImpl;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import lombok.extern.java.Log;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.Stock;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Log
@Scope("prototype")
@Service("huili_")
public class HuiliOperation extends BaseOperation{
    @Override
    public void connect(Account account) throws Exception {
        AndroidDriver<MobileElement> driver = webSessionManager.getConnection(account.getAppLocation());
        String logonPageURL = "https://trading.poems.com.hk/index2.htm";
        driver.get(logonPageURL);

        WebDriverWait wait = new WebDriverWait(driver, 20);


        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@name='login']")));
        //切换到iframe；
        driver.switchTo().frame("login");

        //输入账号、密码；
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='Accode']"))).sendKeys(account.getAccountNo());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='Password']"))).sendKeys(account.getLoginPwd());


        WebElement element =  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='savepasswd']")));
        driver.executeScript("arguments[0].click();",element);

       // 点击 登录
        element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@id='LoginTbl']/tbody/tr[6]/td/input")));
        driver.executeScript("arguments[0].click();",element);

        // 输入pincode
        String pinCode = "";
        element =  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='iOTP']")));

        while(pinCode.length()<6){
            Thread.sleep(500);
            pinCode = element.getAttribute("value");
        }

        element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='LoginBtn']")));

        driver.executeScript("arguments[0].click();",element);

    }

    @Override
    public void logonFinanceIPO(IPOSubscription ipoSubscription) throws Exception {
        this.connect(ipoSubscription.getAccount());
    }

    @Override
    public void prepareFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {

        Account account = ipoSubscription.getAccount();
        AndroidDriver<MobileElement> driver = webSessionManager.getConnection(account.getAppLocation());
        String subscriptionURL = "https://trading.poems.com.hk/Poems2/ProductPlatform/Disclaimer/IPODisclaimer.asp?Popup=N";
        driver.get(subscriptionURL);
        WebDriverWait wait = new WebDriverWait(driver, 20);

        WebElement element =  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='1']/div[2]/div[2]/a[1]")));
        driver.executeScript("arguments[0].click();",element);
    }

    @Override
    public IPOSubscription addFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        return super.addFinanceIPO(ipoSubscription, stock);
    }
}
