package com.chenhe.oauthserver.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author chenhe
 * @date 2019-11-04 15:01
 * @desc
 */
public interface UserDetailsService {
    /**
     * 认证接口
     * @param userName
     * @param password
     * @return
     */
    UserDetails login(String userName, String password);
}
