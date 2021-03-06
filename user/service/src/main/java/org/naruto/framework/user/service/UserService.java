package org.naruto.framework.user.service;

import org.naruto.framework.captcha.CaptchaType;
import org.naruto.framework.captcha.service.CaptchaService;
import org.naruto.framework.core.encrpyt.IEncrpyt;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ServiceException;
import org.naruto.framework.core.file.FileService;
import org.naruto.framework.user.domain.User;
import org.naruto.framework.user.exception.UserError;
import org.naruto.framework.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IEncrpyt encrpytService;

    @Autowired
    private FileService fileService;


    public User register(User user){
        if(user == null) {
            throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        }
        if(null != userRepository.queryUserByMobile(user.getMobile())){
            throw new ServiceException(UserError.USER_EXIST_ERROR);
        }
        if(userRepository.queryUsersByNickname(user.getNickname()).size() > 0){
            throw new ServiceException(UserError.NICKNAME_EXIST_ERROR);
        }
        captchaService.validateCaptcha(user.getMobile(), CaptchaType.SINGUP,user.getCaptcha());

        String salt = UUID.randomUUID().toString().replaceAll("-","");
        user.setPassword(encrpytService.encrpyt(user.getPassword(),salt));
        user.setPasswordSalt(salt);
        return userRepository.save(user);
    }

    public User resetPassword(User user){
        if(user == null || user.getPassword() == null) {
            throw new ServiceException(CommonError.PARAMETER_VALIDATION_ERROR);
        }
        User current = userRepository.queryUserByMobile(user.getMobile());
        if(null == current){
            throw new ServiceException(UserError.USER_NOT_EXIST_ERROR);
        }

        captchaService.validateCaptcha(user.getMobile(), CaptchaType.FORGOTPASSWORD,user.getCaptcha());
        current.setPassword(encrpytService.encrpyt(user.getPassword(),current.getPasswordSalt()));
        return userRepository.save(current);
    }


    public String setAvatar(String imageUrl,String userid) throws Exception{
        User user = this.queryUserById(userid);
        user.setAvatar(imageUrl);
        this.save(user);
        return user.getAvatar();
    }

    public User save(User user){
        Assert.notNull(user,"user can not be null");
        User userWithNickName = userRepository.isNicknameExist(user.getNickname(),user.getId());
        if(userWithNickName!=null){
            throw new ServiceException(UserError.NICKNAME_EXIST_ERROR);
        }
        return userRepository.save(user);
    }

    public User getUserByMobile(String mobile){
        return userRepository.queryUserByMobile(mobile);
    }




    @Transactional
    public User getUserById(String id){ return this.userRepository.findById(id).get(); }

    public Page<User> queryPage(Map map) {
        return userRepository.queryPageByCondition(map);
    }

    //删除单条记录；
    public void delete(String id ){
        this.userRepository.deleteById(id);
    }

    //删除多选记录；
    public void delete(List<String> idList ){
        for(String id:idList) {
            this.userRepository.deleteById(id);
        }
    }

    //查找单条记录；
    public User queryUserById(String id){
        return this.userRepository.findById(id).get();
    }

    public Page<User> queryCustomPage(Map map) {
        return userRepository.queryPageByCondition(map);
    }

    public void increaseArticleCount(String userId,long step){
        userRepository.increateCount(userId,"article_count",step);
    }
    public void increaseStarCount(String userId,Long step){
        userRepository.increateCount(userId,"star_count",step);
    }

    public void increaseLikeCount(String userId,Long step){
        userRepository.increateCount(userId,"like_count",step);
    }

    public void increaseFanCount(String userId,Long step){
        userRepository.increateCount(userId,"fan_count",step);
    }

    public void increaseFollowCount(String userId,Long step){
        userRepository.increateCount(userId,"follow_count",step);
    }
}
