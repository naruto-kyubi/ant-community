package org.naruto.framework.captcha.repository;

import org.naruto.framework.captcha.domain.Captcha;
import org.springframework.data.repository.CrudRepository;

public interface CaptchaRepository extends CrudRepository<Captcha,String> {
//    Captcha findFirstByMobileAndCaptchaOrderByCreateAtDesc(String mobile,String captcha);
    Captcha findFirstByMobileAndTypeAndCaptchaOrderByCreateAtDesc(String mobile, String type, String captcha);

    Captcha findFirstByMobileAndTypeOrderByCreateAtDesc(String mobile, String type);
}
