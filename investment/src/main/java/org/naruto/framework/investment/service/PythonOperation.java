package org.naruto.framework.investment.service;

import com.alibaba.fastjson.JSONObject;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.naruto.framework.investment.common.KeyBordManager;
import org.naruto.framework.investment.connect.SessionManager;
import org.naruto.framework.investment.install.AppInfo;
import org.naruto.framework.investment.install.Apps;
import org.naruto.framework.investment.repository.Account;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@Scope("prototype")
@Service("python")
public class PythonOperation implements AccountOperation {

    @Value("${python.url}")
    private String pythonUrl;

    public RestTemplate restfulTemplate() {
        //复杂构造函数的使用
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000*60*10);// 设置超时
        requestFactory.setReadTimeout(1000*60*10);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(PythonOperation.class);


    @Override
    public void connect(Account account) throws MalformedURLException, InterruptedException {

    }

    @Override
    public Account queryBalance(Account account) throws MalformedURLException, InterruptedException {

        try {
            Map map  =new HashMap();
            map.put("app_location",account.getAppLocation());
            map.put("user_id",account.getAccountNo());
            map.put("login_id",account.getLoginId());
            map.put("login_pwd",account.getLoginPwd());
            map.put("trade_pwd",account.getTradePwd());

            String url = pythonUrl + "/calc/" + account.getType();

            JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);

            String status = result.getString("status");
            String money = result.getString("data");
            if("ok".equals(status)){
                account.setBalance(Float.parseFloat(money));
            }
            return account;
        } catch (RestClientException e) {
            e.printStackTrace();
            throw e;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
