package org.naruto.framework.investment.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.java.Log;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.repository.FundTrans;
import org.naruto.framework.investment.repository.IPOSubscription;
import org.naruto.framework.investment.repository.Stock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
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
        HttpRequestRetryHandler requestRetryHandler = new DefaultHttpRequestRetryHandler(0, false);
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(1000*60*10);
        httpRequestFactory.setConnectTimeout(1000*60*10);// 设置超时
        httpRequestFactory.setReadTimeout(1000*60*10);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(httpRequestFactory);

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
            JSONObject obj = result.getJSONObject("data");
            if("ok".equals(status)){

                account.setBalance(Float.parseFloat(obj.getString("balance")));
                account.setCash(Float.parseFloat(obj.getString("cash")));
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
        map.put("pin_pwd",account.getPinPwd());
        map.put("stock_count",stock.getLot());

        String url = pythonUrl + "/one_cash";
        System.out.println("url="+url);
        JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);

        String status = result.getString("status");

        if("ok".equals(status)){
            ipoSubscription.setNumberOfShares(stock.getLot());
            //其它费用有待完善；
            return ipoSubscription;
        }else
        {
            throw new Exception();
        }
    }
//
//    private static InputStream doPostAndGetStream(String urlPath, Map<Object,Object>params){
//
//        InputStream inputStream =  null ;
//        OutputStream outputStream = null ;
//        URL url = null ;
//        try {
//            url = new URL(urlPath);
//            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
//            httpURLConnection.setConnectTimeout(1000*60*10);
//            httpURLConnection.setReadTimeout(1000*60*10);
//            httpURLConnection.setDoInput(true);
//            httpURLConnection.setDoOutput(true);
//
//            httpURLConnection.setRequestMethod("POST");
//            StringBuffer stringBuffer = new StringBuffer();
//            for(Map.Entry<Object,Object> entry:params.entrySet()){
//                stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
//            }
//            stringBuffer.deleteCharAt(stringBuffer.length()-1);
//
//            outputStream = httpURLConnection.getOutputStream();
//            outputStream.write(stringBuffer.toString().getBytes());
//
//            inputStream = httpURLConnection.getInputStream();
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//
//        return inputStream ;
//
//    }
//    private static String streamToString(InputStream inputStream,String encodeType){
//        String resultString = null ;
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        int len = 0;
//        byte data[]=new byte[1024];
//        try {
//            while((len=inputStream.read(data))!=-1){
//                byteArrayOutputStream.write(data,0,len);
//            }
//            byte[]allData = byteArrayOutputStream.toByteArray();
//            resultString = new String(allData,encodeType);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//
//        return resultString ;
//    }
//
//    public static String doPost(String urlPath,Map<Object,Object>params,String encodeType){
//        InputStream  inputStream = doPostAndGetStream(urlPath,params);
//        String resultString = streamToString(inputStream, encodeType);
//        return resultString ;
//    }


    public void logonFinanceIPO(IPOSubscription ipoSubscription) throws Exception {

        Map map = new HashMap();
        Account account = ipoSubscription.getAccount();
        map.put("app_location", account.getAppLocation());
        map.put("bond_id", account.getAccountType().getId());
        map.put("account_no", account.getAccountNo());
        map.put("user_id", account.getAccountNo());
        map.put("login_id", account.getLoginId());
        map.put("login_pwd", account.getLoginPwd());
        map.put("trade_pwd", account.getTradePwd());
        map.put("pin_pwd", account.getPinPwd());
        map.put("logon_type", "finance");
        String url = pythonUrl + "/finance/logon";

        JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);

        String status = result.getString("status");

        if (!"ok".equals(status)) {
            throw new Exception();
        }
    }

    public void prepareFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {

        Map map  =new HashMap();
        Account account = ipoSubscription.getAccount();
        map.put("stock_no",stock.getCode());
        map.put("app_location",account.getAppLocation());
        map.put("bond_id",account.getAccountType().getId());
        map.put("account_no",account.getAccountNo());
        map.put("user_id",account.getAccountNo());
        map.put("login_id",account.getLoginId());
        map.put("login_pwd",account.getLoginPwd());
        map.put("trade_pwd",account.getTradePwd());
        map.put("pin_pwd",account.getPinPwd());
        Integer stock_count = ipoSubscription.getPlanSubscriptionShares();
        if(stock_count<stock.getLot()){
            stock_count = stock.getLot();
        }
        map.put("stock_count",stock_count);

        String url = pythonUrl + "/finance/parepare";

        JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);

        String status = result.getString("status");

        if(!"ok".equals(status)){
            throw new Exception();
        }
    }

    public IPOSubscription addFinanceIPO(IPOSubscription ipoSubscription, Stock stock)  throws Exception {

        Map map  =new HashMap();
        Account account = ipoSubscription.getAccount();
        map.put("stock_no",stock.getCode());
        map.put("app_location",account.getAppLocation());
        map.put("bond_id",account.getAccountType().getId());
        map.put("account_no",account.getAccountNo());
        map.put("user_id",account.getAccountNo());
        map.put("login_id",account.getLoginId());
        map.put("login_pwd",account.getLoginPwd());
        map.put("trade_pwd",account.getTradePwd());
        map.put("pin_pwd",account.getPinPwd());
        Integer stock_count = ipoSubscription.getPlanSubscriptionShares();
        if(stock_count<stock.getLot()){
            stock_count = stock.getLot();
        }
        map.put("stock_count",stock_count);

        String url = pythonUrl + "/finance/start";

        JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);

        String status = result.getString("status");

        if("ok".equals(status)){
            ipoSubscription.setNumberOfShares(stock.getLot());
            //其它费用有待完善；
            return ipoSubscription;
        }else
        {
            throw new Exception();
        }
    }

    @Override
    public IPOSubscription cancelFinanceIPO(IPOSubscription ipoSubscription, Stock stock) throws Exception {
        Map map  =new HashMap();
        Account account = ipoSubscription.getAccount();
        map.put("stock_no",stock.getCode());
        map.put("app_location",account.getAppLocation());
        map.put("bond_id",account.getAccountType().getId());
        map.put("user_id",account.getAccountNo());
        map.put("login_id",account.getLoginId());
        map.put("login_pwd",account.getLoginPwd());
        map.put("trade_pwd",account.getTradePwd());
        map.put("pin_pwd",account.getPinPwd());
        map.put("stock_count",stock.getLot());

        String url = pythonUrl + "/finance/quit";
        JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);

        String status = result.getString("status");

        if("ok".equals(status)){
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
        map.put("pin_pwd",account.getPinPwd());

        String url = pythonUrl + "/sign";
        System.out.println("url="+url);
        JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);

        String status = result.getString("status");
        String data = result.getString("data");

        if("ok".equals(status)){
            if("1".equals(data))
                //中签
                ipoSubscription.setNumberOfSigned(stock.getLot());
            else{
                ipoSubscription.setNumberOfSigned(Integer.parseInt(data));
            }
            return ipoSubscription;
        }else
        {
            throw new Exception();
        }
    }

    @Override
    public FundTrans executeTrans(FundTrans fundTrans, Account account, Account BankAccount) throws Exception {
//        {
//            bond:{
//                mobile_id,
//                        bond_id,
//                        user_id,
//                        login_id,
//                        login_pwd,
//                        trade_pwd,
//                        capital_account,
//                        money,
//            },
//            bank:{
//                mobile_id,
//                        user_id,
//                        login_id,
//                        login_pwd,
//
//            }
//        }

        Map map  =new HashMap();
        Map bondMap = new HashMap();
        bondMap.put("mobile_id",account.getAppLocation());
        bondMap.put("bond_id",account.getAccountType().getId());
        bondMap.put("user_id",account.getAccountNo());
        bondMap.put("login_id",account.getLoginId());
        bondMap.put("login_pwd",account.getLoginPwd());
        bondMap.put("trade_pwd",account.getTradePwd());
        bondMap.put("pin_pwd",account.getPinPwd());
        bondMap.put("capital_account",account.getCapitalAccount());
        bondMap.put("money",fundTrans.getAmount());

        map.put("bond",bondMap);

        Map bankMap = new HashMap();
        bankMap.put("mobile_id",BankAccount.getAppLocation());
        bankMap.put("user_id",BankAccount.getAccountNo());
        bankMap.put("login_id",BankAccount.getLoginId());
        bankMap.put("login_pwd",BankAccount.getLoginPwd());
        map.put("bank",bankMap);

        if(account.equals(fundTrans.getDebitAccount())){
            //券商出金；
            String url = pythonUrl + "/out_money";
            JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);

            String status = result.getString("status");
            String data = result.getString("data");
            if(!"ok".equals(status)){
                throw new Exception();
            }

        }else{
            //券商入金；
            String url = pythonUrl + "/in_money";
            JSONObject result = restfulTemplate().postForObject(url, map, JSONObject.class);

            String status = result.getString("status");
            String data = result.getString("data");
            if(!"ok".equals(status)){
                throw new Exception();
            }
        }

        return fundTrans;
    }
}
