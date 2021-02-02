package org.naruto.framework.investment.connect;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSessionManager {
    //存放key的格式为mobileid
    public static Map<String, AndroidDriver<MobileElement>> drivers = new HashMap<String, AndroidDriver<MobileElement>>();

    //没一个session的任务执行阀门
    public static Map<String, Boolean> taskStopSigns = new ConcurrentHashMap<String, Boolean>();

    public AndroidDriver<MobileElement> connect(String mobileId) throws MalformedURLException {
        //  AppInfo appinfo = Apps.apps.get(appName);
        DesiredCapabilities capabilities = new DesiredCapabilities();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.setCapability(CapabilityType.BROWSER_NAME,"Chrome");
        options.setCapability("androidUseRunningApp",true);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

//      capabilities.setCapability("deviceName", "192.168.1.106:5555");
        capabilities.setCapability("deviceName", "HONOR 20 Lite");
//      capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "10.0");
        capabilities.setCapability("noReset", "True");
//      capabilities.setCapability("browserName","Chrome");


        capabilities.setCapability("newCommandTimeout", "36000");
        capabilities.setCapability("udid", mobileId);
//        capabilities.setCapability("sessionOverride",true);
//      capabilities.setCapability("automationName", "uiautomator2");


        AndroidDriver<MobileElement> driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        drivers.put(mobileId,driver);
        return driver;
    }

    public AndroidDriver<MobileElement> getConnection(String mobileId) throws MalformedURLException {
        AndroidDriver<MobileElement> driver = drivers.get(mobileId);

        if(null==driver){
            return this.connect(mobileId);
        }else {
            try {
                driver.getBatteryInfo();
            } catch (Exception e) {
                //重新连接
                return this.connect(mobileId);
            }
        }
        return driver;
    }

    public void addStopSing(String mobileId){
        this.taskStopSigns.put(mobileId,Boolean.valueOf(true));
    }

    public boolean shouldBeStoped(String mobileId){
        Boolean shouldBeStoped = this.taskStopSigns.get(mobileId);
        if(null!=shouldBeStoped){
            //
            this.taskStopSigns.clear();
            return shouldBeStoped.booleanValue();
        }
        return false;
    }
}
