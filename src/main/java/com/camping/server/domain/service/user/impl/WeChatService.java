package com.camping.server.domain.service.user.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeChatService {

    // 定义微信小程序的AppID和AppSecret
    @Value("${wechat.mini.appid}")
    String appid;

    @Value("${wechat.mini.secret}")
    String secret;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    // 定义微信登录接口的URL
    private static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    // 定义一个方法，根据微信小程序传来的code，获取用户的openid和session_key
    public WxResponse code2Session(String code) throws IllegalArgumentException {
        // 构造请求参数
        String params = "?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        // 发送GET请求，获取响应结果
        ResponseEntity<String> response = restTemplate.getForEntity(WECHAT_LOGIN_URL + params, String.class);

        // 判断响应状态是否为200
        if (response.getStatusCode() == HttpStatus.OK) {
            String body = response.getBody();
            try {
                return objectMapper.readValue(body, WxResponse.class);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        throw new IllegalArgumentException(code + "is not valid code");
    }

    @Data
    public static class WxResponse {
        @JsonProperty("openid")
        private String openid;
        @JsonProperty("unionid")
        private String unionid;
        @JsonProperty("errcode")
        private String errcode;
        @JsonProperty("errmsg")
        private String errmsg;
    }
}

