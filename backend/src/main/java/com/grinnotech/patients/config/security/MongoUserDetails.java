package com.grinnotech.patients.config.security;

import com.grinnotech.patients.dao.UserRepository;
import com.grinnotech.patients.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.toList;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.util.StringUtils.hasText;

public class MongoUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Collection<GrantedAuthority> authorities;

    private final Collection<GrantedAuthority> userAuthorities;

    private final String password;

    private final String email;

    private final boolean enabled;

    private final String userDbId;

    private final boolean locked;

    private final Locale locale;

    private boolean screenLocked;

    public MongoUserDetails(User user) {
        this.userDbId = user.getId();

        this.password = user.getPasswordHash();
        this.email = user.getEmail();
        this.enabled = user.isEnabled();

        if (hasText(user.getLocale())) {
            this.locale = new Locale(user.getLocale());
        } else {
            this.locale = Locale.ENGLISH;
        }

        this.locked = user.getLockedOutUntil() != null && user.getLockedOutUntil().after(new Date());

        this.userAuthorities = user.getAuthorities() != null ? createAuthorities(user.getAuthorities()) : emptySet();

        this.authorities = unmodifiableCollection(
                hasText(user.getSecret()) ? createAuthorityList("PRE_AUTH") : this.userAuthorities);
    }

    public boolean isPreAuth() {
        return hasAuthority("PRE_AUTH");
    }

    public void grantAuthorities() {
        this.authorities = unmodifiableCollection(this.userAuthorities);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public User getUser(UserRepository userRepository) {
        return userRepository.findOneActive(getUserDbId());
    }

    public String getUserDbId() {
        return this.userDbId;
    }

    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean hasAuthority(String authority) {
        return getAuthorities().stream().anyMatch(a -> authority.equals(a.getAuthority()));
    }

    public boolean isScreenLocked() {
        return this.screenLocked;
    }

    public void setScreenLocked(boolean screenLocked) {
        this.screenLocked = screenLocked;
    }

    private static Set<GrantedAuthority> createAuthorities(Collection<String> stringAuthorities) {
        return stringAuthorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

}
