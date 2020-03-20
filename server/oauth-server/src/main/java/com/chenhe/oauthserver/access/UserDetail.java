package com.chenhe.oauthserver.access;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author chenhe
 * @date 2019-11-07 16:36
 * @desc
 */
public class UserDetail implements UserDetails , CredentialsContainer , Serializable {
    private final UserInfo userInfo;
    private final User user;

    public UserDetail(UserInfo userInfo,User user) {
        this.userInfo = userInfo;
        this.user=user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public void eraseCredentials() {
        user.eraseCredentials();
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
