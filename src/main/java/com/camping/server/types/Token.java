package com.camping.server.types;

import com.camping.server.common.exception.InvalidTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

@Data
public class Token {

    private static final String KEY = "chm1234569543210";

    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String ALGORITHM = "AES";

    private static final int EXPIRE_HOURS = 12;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Long uid;
    private long createdAt;
    private String random;

    public boolean checkExpire(ZoneId zoneId){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt),zoneId).plusHours(EXPIRE_HOURS).isBefore(LocalDateTime.now());
    }

    public LocalDateTime getExpireTime(ZoneId zoneId){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt),zoneId).plusHours(EXPIRE_HOURS);
    }

    public static Token parse(String cipherText) throws InvalidTokenException {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
            byte[] decryptedBytes = cipher.doFinal(cipherBytes);

            return objectMapper.readValue(new String(decryptedBytes, StandardCharsets.UTF_8), Token.class);
        } catch (Exception e) {
            throw new InvalidTokenException(e);
        }
    }

    public String encrypt() throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(objectMapper.writeValueAsString(this).getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

}
