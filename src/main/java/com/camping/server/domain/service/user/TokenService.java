package com.camping.server.domain.service.user;

import com.camping.server.common.exception.InvalidTokenException;
import com.camping.server.types.Token;

public interface TokenService {
    Token generateToken(Long uid);

    Token verifyToken(String encryptedToken) throws InvalidTokenException;
}
