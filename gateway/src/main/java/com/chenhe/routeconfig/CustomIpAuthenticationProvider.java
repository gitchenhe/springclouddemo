package com.chenhe.routeconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * @author chenhe
 * @date 2019-11-04 14:08
 * @desc
 */
@Slf4j
@Component
public class CustomIpAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
        String userIp = webAuthenticationDetails.getRemoteAddress();

        log.info("========authenticate=======");
        authentication.setAuthenticated(
                false
        );
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
