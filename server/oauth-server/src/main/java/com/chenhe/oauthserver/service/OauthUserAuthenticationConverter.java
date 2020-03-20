package com.chenhe.oauthserver.service;

import com.chenhe.oauthserver.access.JwtAccessToken;
import com.chenhe.oauthserver.access.UserDetail;
import com.chenhe.oauthserver.access.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.*;

/**
 * @author chenhe
 * @date 2019-11-07 18:05
 * @desc
 */
@Slf4j
public class OauthUserAuthenticationConverter implements UserAuthenticationConverter {
    private final String USER_INFO = JwtAccessToken.USER_INFO;

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {
        UserInfo userInfo = ((UserDetail) userAuthentication.getPrincipal()).getUserInfo();
        Map map = new HashMap(2);
        map.put(AUTHORITIES, userAuthentication.getAuthorities());
        log.info("这里调用到了");
        return map;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Set<String> authorities = (HashSet) map.get(AUTHORITIES);
            List<GrantedAuthority> authorityList = new ArrayList<>();
            Iterator<String> iterator = authorities.iterator();
            while (iterator.hasNext()) {
                authorityList.add(new SimpleGrantedAuthority(iterator.next()));
            }

            UserInfo userInfo = (UserInfo) map.get(USER_INFO);
            return new UsernamePasswordAuthenticationToken(userInfo, "N_A", authorityList);
        }
        return null;
    }
}
