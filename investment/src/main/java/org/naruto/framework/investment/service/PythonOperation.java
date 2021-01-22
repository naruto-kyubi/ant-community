package org.naruto.framework.investment.service;

import com.alibaba.fastjson.JSONObject;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import lombok.extern.java.Log;
import org.naruto.framework.investment.common.KeyBordManager;
import org.naruto.framework.investment.connect.SessionManager;
import org.naruto.framework.investment.install.AppInfo;
import org.naruto.framework.investment.install.Apps;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.FundTrans;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.Stock;
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
@Log
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


    @Override
    public void connect(Account account) throws MalformedURLException, InterruptedException {
        Map map  =new HashMap();
        map.put("app_location",account.getAppLocation());
        map.put("user_id",account.getAccountNo());
        map.put("login_id",account.getLoginId());
        map.put("login_pwd",account.getLoginPwd());
        map.put("trade_pwd",account.getTradePwd());
        map.put("bond_id",account.getAccountType().getId());
        String url = pythonUrl + "/logon";

        JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);
//
//        String status = result.getString("status");
//        String money = result.getString("data");
//        if("ok".equals(status) && !money.equals("-1")){
////            account.setBalance(Float.parseFloat(money));
////            return account;
//        }else
//        {
////            throw new Exception();
//        }
    }

    @Override
    public Account queryBalance(Account account) throws Exception {

            Map map  =new HashMap();
            map.put("app_location",account.getAppLocation());
            map.put("user_id",account.getAccountNo());
            map.put("login_id",account.getLoginId());
            map.put("login_pwd",account.getLoginPwd());
            map.put("trade_pwd",account.getTradePwd());

            String url = pythonUrl + "/calc/" + account.getAccountType().getId();

            JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);

            String status = result.getString("status");
            String money = result.getString("data");
            if("ok".equals(status) && !money.equals("-1")){
                account.setBalance(Float.parseFloat(money));
                return account;
            }else
            {
                throw new Exception();
            }

    }

    @Override
    public IPOSubscription oneCash(IPOSubscription ipoSubscription, Stock stock) throws Exception {

        Map map  =new HashMap();
        Account account = ipoSubscription.getAccount();
        map.put("stock_no",stock.getCode());
        map.put("app_location",account.getAppLocation());
        map.put("bond_id",account.getAccountType().getId());
        map.put("user_id",account.getAccountNo());
        map.put("login_id",account.getLoginId());
        map.put("login_pwd",account.getLoginPwd());
        map.put("trade_pwd",account.getTradePwd());
        map.put("stock_count",stock.getLot());

        String url = pythonUrl + "/one_cash";
        System.out.println("url="+url);
        JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);

        String status = result.getString("status");
        String money = result.getString("data");

        if("ok".equals(status) && !money.equals("-1")){
            ipoSubscription.setNumberOfShares(stock.getLot());
            //其它费用有待完善；
            return ipoSubscription;
        }else
        {
            throw new Exception();
        }
    }

    @Override
    public IPOSubscription sign(IPOSubscription ipoSubscription, Stock stock) throws Exception{
        Map map  =new HashMap();
        Account account = ipoSubscription.getAccount();
        map.put("stock_no",stock.getCode());
        map.put("app_location",account.getAppLocation());
        map.put("bond_id",account.getAccountType().getId());
        map.put("user_id",account.getAccountNo());
        map.put("login_id",account.getLoginId());
        map.put("login_pwd",account.getLoginPwd());
        map.put("trade_pwd",account.getTradePwd());

        String url = pythonUrl + "/sign";
        System.out.println("url="+url);
        JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);

        String status = result.getString("status");
        String data = result.getString("data");

        if("ok".equals(status) && Integer.parseInt(data)>0){
            ipoSubscription.setNumberOfSigned(stock.getLot());
            //其它费用有待完善；
        }else
        {
            ipoSubscription.setNumberOfSigned(Integer.valueOf(data));
        }
        return ipoSubscription;
    }

    @Override
    public FundTrans executeTrans(FundTrans fundTrans) throws Exception {
        log.info("We are transing here ..................................................");
        return null;
    }
}
