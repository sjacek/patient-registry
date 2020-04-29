package com.grinnotech.patients.config.security;

import static com.grinnotech.patients.model.Authority.PRE_AUTH;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.toSet;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.util.StringUtils.hasText;

import com.grinnotech.patients.mongodb.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

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
                hasText(user.getSecret()) ? createAuthorityList(PRE_AUTH.name()) : this.userAuthorities);
    }

    public boolean isPreAuth() {
        return hasAuthority(PRE_AUTH.name());
    }

    public void grantAuthorities() {
        authorities = unmodifiableCollection(userAuthorities);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getUserDbId() {
        return userDbId;
    }

    public Locale getLocale() {
        return locale;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean hasAuthority(String authority) {
        return getAuthorities().stream().anyMatch(ga -> authority.equals(ga.getAuthority()));
    }

    public boolean isScreenLocked() {
        return screenLocked;
    }

    public void setScreenLocked(boolean screenLocked) {
        this.screenLocked = screenLocked;
    }

    private static Set<GrantedAuthority> createAuthorities(Collection<String> stringAuthorities) {
        return stringAuthorities.stream().map(SimpleGrantedAuthority::new).collect(toSet());
    }

}
