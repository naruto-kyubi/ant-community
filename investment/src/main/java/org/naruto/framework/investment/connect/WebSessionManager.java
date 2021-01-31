package org.naruto.framework.investment.connect;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSessionManager {
    //存放key的格式为mobileid
    public static Map<String, AndroidDriver<MobileElement>> drivers = new HashMap<String, AndroidDriver<MobileElement>>();

    public AndroidDriver<MobileElement> connect(String mobileId) throws MalformedURLException {
        //  AppInfo appinfo = Apps.apps.get(appName);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        //  capabilities.setCapability("deviceName", "192.168.1.106:5555");
        capabilities.setCapability("deviceName", "HONOR 20 Lite");
//        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "10.0");
        capabilities.setCapability("browserName","Chrome");
        //   capabilities.setCapability("appPackage", appinfo.getAppPackage());
        //   capabilities.setCapability("appActivity", appinfo.getAppActivity());
        //    capabilities.setCapability("noReset", "True");
        capabilities.setCapability("newCommandTimeout", "36000");
        capabilities.setCapability("udid", mobileId);
//        capabilities.setCapability("automationName", "uiautomator2");


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
}
