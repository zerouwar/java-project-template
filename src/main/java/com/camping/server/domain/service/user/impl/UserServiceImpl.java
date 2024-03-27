package com.camping.server.domain.service.user.impl;

import com.camping.server.domain.entity.User;
import com.camping.server.domain.repository.UserRepository;
import com.camping.server.domain.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    WeChatService weChatService;

    @Autowired
    UserRepository userRepository;
    @Override
    public User wxLoginWithAutoSignup(String code) {
        WeChatService.WxResponse wxResponse = weChatService.code2Session(code);

        if(StringUtils.isEmpty(wxResponse.getOpenid())){
            throw new IllegalArgumentException("微信登录失败");
        }

        return userRepository.findByOpenId(wxResponse.getOpenid()).orElseGet(()->{
            User newUser = new User();
            newUser.setOpenId(wxResponse.getOpenid());
            return userRepository.save(newUser);
        });
    }
}
