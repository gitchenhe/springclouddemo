package com.chenhe.oauthserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author chenhe
 * @date 2019-11-04 15:01
 * @desc
 */
@Service
public class SSOUserDetailsService implements UserDetailsService {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails login(String userName, String password) {
        if ("admin".equals(userName) && "123456".equals(password)) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return new User(userName,password, AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
        //return new User(userName, passwordEncoder.encode(password), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
