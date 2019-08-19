package org.naruto.framework.captcha.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.naruto.framework.captcha.CaptchaConfig;
import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.domain.Captcha;
import org.naruto.framework.captcha.exception.CaptchaError;
import org.naruto.framework.captcha.repository.CaptchaRepository;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Service
@Slf4j
@Data
public class CaptchaService {

    @Autowired
    CaptchaRepository captchaRepository;

    @Autowired
    private CaptchaConfig captchaConfig;

    public Captcha getCaptcha(String mobile, CaptchaType captchaType){
        if(StringUtils.isBlank(mobile) ||  null==captchaType ) throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);

        Captcha otp = captchaRepository.findFirstByMobileAndTypeOrderByCreateAtDesc(mobile,captchaType.toString());

        if(null==otp)  throw new ServiceException(CaptchaError.CAPTCHA_INCORRECT_ERROR);
        long duration = Duration.between(otp.getCreateAt().toInstant(), Instant.now()).getSeconds();
        if (duration > captchaConfig.getExpiryDate()) throw new ServiceException(CaptchaError.CAPTCHA_TIMEOUT_ERROR);
        return otp;
    }

    public Captcha createCaptcha(String mobile, CaptchaType captchaType){
        if(StringUtils.isBlank(mobile) || null==captchaType){
            throw  new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR.setErrMsg("PhoneNumber or type is mandatory"));
        }
        Captcha captcha = new Captcha();
        captcha.setMobile(mobile);
        captcha.setType(captchaType.toString());
        captcha.setCreateAt(new Date());

        Random random = new Random();
        String otp = Integer.toString(1000 + random.nextInt(9000));

        log.info("The otp code is :" + otp);
        captcha.setCaptcha(otp);
        sendSMS(mobile, otp);
        return captchaRepository.save(captcha);
    }

    public void validateCaptcha(String mobile, CaptchaType captchaType, String captcha){
        if(StringUtils.isBlank(mobile) ||  null==captchaType || StringUtils.isBlank(captcha)) throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);

        Captcha otp = captchaRepository.findFirstByMobileAndTypeAndCaptchaOrderByCreateAtDesc(mobile,captchaType.toString(),captcha);
        if(null==otp)  throw new ServiceException(CaptchaError.CAPTCHA_INCORRECT_ERROR);
        long duration = Duration.between(otp.getCreateAt().toInstant(), Instant.now()).getSeconds();
        if (duration > captchaConfig.getExpiryDate()) throw new ServiceException(CaptchaError.CAPTCHA_TIMEOUT_ERROR);
    }

    private void sendSMS(String mobile, String content) {
        DefaultProfile profile = DefaultProfile.getProfile(captchaConfig.getRegionId(), captchaConfig.getAccessKey(), captchaConfig.getAccessSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain(captchaConfig.getDomain());
        request.setVersion(captchaConfig.getVersion());
        request.setAction("SendSms");
        request.putQueryParameter("RegionId",captchaConfig.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", captchaConfig.getSignName());
        request.putQueryParameter("TemplateCode", captchaConfig.getTemplateCode());

        request.putQueryParameter("TemplateParam", "{\"code\":" + String.valueOf(content) + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info(response.getData());
            JSONObject jsonObject = JSONObject.parseObject(response.getData());
            String code = jsonObject.getString("Code");
            if(!"OK".equals(code)) {
                String message = jsonObject.getString("Message");
                throw  new ServiceException(CaptchaError.CAPTCHA_SERVICE_ERROR.setErrMsg(message));
            }
        } catch (ServerException e) {
            log.error(e.getErrMsg());
            throw new ServiceException(CaptchaError.CAPTCHA_SERVICE_ERROR);
        } catch (ClientException e) {
            throw new ServiceException(CaptchaError.CAPTCHA_SERVICE_ERROR);
        }
    }
}
