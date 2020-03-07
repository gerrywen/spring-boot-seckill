package com.gerrywen.seckill.service;

import com.gerrywen.seckill.controller.LoginController;
import com.gerrywen.seckill.dao.MiaoshaUserDao;
import com.gerrywen.seckill.domain.MiaoshaUser;
import com.gerrywen.seckill.dto.LoginDTO;
import com.gerrywen.seckill.exception.GlobalException;
import com.gerrywen.seckill.result.CodeMsg;
import com.gerrywen.seckill.third.redis.RedisService;
import com.gerrywen.seckill.third.redis.constant.UserKey;
import com.gerrywen.seckill.util.MD5Util;
import com.gerrywen.seckill.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gerrywen.seckill.third.redis.enums.CtimsModelEnum;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * program: spring-boot-seckill->MiaoshaUserService
 * description:
 * author: gerry
 * created: 2020-03-05 21:01
 **/
@Service
public class MiaoshaUserService {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    public static final String COOKI_NAME_TOKEN = "token";
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;


    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;


    public MiaoshaUser getById(long id) {
        MiaoshaUser user = redisService.get(CtimsModelEnum.CTIMS_USER_CAP, UserKey.USER_ID_KEY_PREFIX + id, MiaoshaUser.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = miaoshaUserDao.getById(id);
        if (user != null) {
            redisService.set(CtimsModelEnum.CTIMS_USER_CAP, UserKey.USER_ID_KEY_PREFIX + id, user, 7200);
        }
        return user;
    }

    public boolean updatePassword(String token, long id, String formPass) {
        //取user
        MiaoshaUser user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //处理缓存
        redisService.del(CtimsModelEnum.CTIMS_USER_CAP, UserKey.USER_ID_KEY_PREFIX + id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(CtimsModelEnum.CTIMS_USER_CAP, UserKey.USER_TOKEN_KEY_PREFIX + token, user, 7200);
        return true;
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(CtimsModelEnum.CTIMS_USER_CAP, UserKey.USER_TOKEN_KEY_PREFIX + token, MiaoshaUser.class);
        //延长有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    public String login(HttpServletResponse response, LoginDTO loginDTO) {
        if (loginDTO == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginDTO.getMobile();
        String formPass = loginDTO.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        log.info("加盐:{}, 加密后的密码: {}", saltDB, calcPass);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;

    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(CtimsModelEnum.CTIMS_USER_CAP, UserKey.USER_TOKEN_KEY_PREFIX + token, user, 7200);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(TOKEN_EXPIRE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
