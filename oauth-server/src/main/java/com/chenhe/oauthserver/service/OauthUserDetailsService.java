package com.chenhe.oauthserver.service;

import com.chenhe.oauthserver.access.UserDetail;
import com.chenhe.oauthserver.access.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author chenhe
 * @date 2019-11-04 15:01
 * @desc 从db查询用户信息
 */
@Slf4j
@Service
public class OauthUserDetailsService implements UserDetailsService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 扩展用户信息
     * @param userName
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserInfo userInfo = jdbcTemplate.queryForObject("select id,account,real_name realName,age,enable,password,address from user_info where account = ?",new Object[]{userName},new BeanPropertyRowMapper<UserInfo>(UserInfo.class));
        if (userInfo == null){
            throw new UsernameNotFoundException("用户不存在:"+userName);
        }

        log.info("用户信息:{}",userInfo);

        User user = new User(userName, userInfo.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));

        //用户类的包装器,包含了用户具体信息
        UserDetail userDetail = new UserDetail(userInfo, user);
        return userDetail;
    }

    public static void main(String[] args) {
        System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("654321"));
    }



}
