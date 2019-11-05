package com.chenhe.oauthserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author chenhe
 * @date 2019-11-04 15:01
 * @desc
 */
@Service
public class SSOUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (!"admin".equals(userName) ) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return new User(userName,passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }

    public static void main(String[] args) {
        System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("654321"));
    }



}
