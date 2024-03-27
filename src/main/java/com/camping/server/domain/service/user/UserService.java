package com.camping.server.domain.service.user;


import com.camping.server.domain.entity.User;

import java.util.Optional;

public interface UserService {

    /**
     * 微信登录，自动注册
     */
    User wxLoginWithAutoSignup(String code);
}
