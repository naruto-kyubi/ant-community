package org.naruto.framework.investment.connect;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.naruto.framework.investment.install.AppInfo;
import org.naruto.framework.investment.install.Apps;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
public class SessionManager {
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
     //   capabilities.setCapability("appPackage", appinfo.getAppPackage());
     //   capabilities.setCapability("appActivity", appinfo.getAppActivity());
        capabilities.setCapability("noReset", "True");
        capabilities.setCapability("newCommandTimeout", "2000");
        capabilities.setCapability("udid", mobileId);
        capabilities.setCapability("automationName", "uiautomator2");


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

    public AndroidDriver<MobileElement> activateApp(String mobileId,String appName) throws MalformedURLException {
        AppInfo appInfo = Apps.apps.get(appName);
        AndroidDriver<MobileElement> driver = this.getConnection(mobileId);
        /*
        0 is not installed. 1 is not running. 2 is running in background or suspended. 3 is running in background. 4 is running in foreground.
         */
        ApplicationState appState = driver.queryAppState(appInfo.getAppPackage());
        if(appState.compareTo(ApplicationState.NOT_RUNNING) > 1){
            driver.activateApp(appInfo.getAppPackage());
        }else{
            try {
                driver.startActivity(new Activity(appInfo.getAppPackage(), appInfo.getAppActivity()));
            }catch(Exception e){
                driver.activateApp(appInfo.getAppPackage());
            }
        }
        return driver;
    }
}
