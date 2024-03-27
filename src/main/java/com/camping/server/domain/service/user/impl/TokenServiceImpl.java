package com.camping.server.domain.service.user.impl;


import com.camping.server.common.exception.InvalidTokenException;
import com.camping.server.domain.service.user.TokenService;
import com.camping.server.types.Token;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger LOG = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Autowired
    ZoneId zoneId;

    public Token generateToken(Long uid) {
        Token token = new Token();
        token.setUid(uid);
        token.setCreatedAt(LocalDateTime.now().atZone(zoneId).toInstant().toEpochMilli());
        token.setRandom(RandomStringUtils.random(6,true,true));
        return token;
    }

    public Token verifyToken(String tokenStr) throws InvalidTokenException {
        return Token.parse(tokenStr);
    }
}
