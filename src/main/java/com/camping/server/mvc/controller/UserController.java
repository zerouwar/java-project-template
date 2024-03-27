package com.camping.server.mvc.controller;


import com.camping.server.common.authorize.MustLogin;
import com.camping.server.common.exception.InvalidTokenException;
import com.camping.server.domain.entity.User;
import com.camping.server.domain.repository.UserRepository;
import com.camping.server.domain.service.user.TokenService;
import com.camping.server.domain.service.user.UserService;
import com.camping.server.types.Token;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    ZoneId zoneId;

    @GetMapping("/wxLogin")
    public User login(@RequestParam String code, HttpServletResponse response) throws Exception {
        User user = userService.wxLoginWithAutoSignup(code);
        Token token = tokenService.generateToken(user.getId());
        response.setHeader("Authorization", token.encrypt());
        response.setHeader("Token-Expire", token.getExpireTime(zoneId).format(DateTimeFormatter.ISO_DATE_TIME));

        return user;
    }

    @GetMapping("/authorize")
    @MustLogin
    public Long authorize(@RequestAttribute("token") Token token) {
        return token.getUid();
    }

    @GetMapping("/me")
    @MustLogin
    public User me(@RequestAttribute("token") Token token) throws InvalidTokenException {
        return userRepository.findById(token.getUid()).orElseThrow(InvalidTokenException::new);
    }
}
