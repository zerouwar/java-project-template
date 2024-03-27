package com.camping.server.common.authorize;


import com.camping.server.common.exception.InvalidTokenException;
import com.camping.server.domain.repository.UserRepository;
import com.camping.server.domain.service.user.TokenService;
import com.camping.server.types.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.ZoneId;

@Component
public class MustLoginInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(MustLoginInterceptor.class);

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ZoneId zoneId;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MustLogin mustLogin;
        if (handler instanceof HandlerMethod) {
            mustLogin = ((HandlerMethod) handler).getMethodAnnotation(MustLogin.class);
        } else {
            return true;
        }

        if (mustLogin == null) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            this.notAuthorized(response);
            return false;
        }

        try {
            Token verifyToken = tokenService.verifyToken(token);

            if(verifyToken.checkExpire(zoneId)){
                this.notAuthorized(response);
                return false;
            }

            request.setAttribute("token", verifyToken);

            return true;
        } catch (InvalidTokenException e) {
            logger.error("非法token", e);
            this.notAuthorized(response);
            return false;
        }
    }

    private void notAuthorized(HttpServletResponse response){
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
