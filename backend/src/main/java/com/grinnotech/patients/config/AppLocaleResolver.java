package com.grinnotech.patients.config;

import com.grinnotech.patients.config.security.MongoUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 *
 * @author Jacek Sztajnke
 */
public class AppLocaleResolver extends AbstractLocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return request.getLocale();
        }
        
        if (authentication.getPrincipal() instanceof MongoUserDetails) {
            return ((MongoUserDetails) authentication.getPrincipal()).getLocale();
        }
        
        if (getDefaultLocale() != null) {
            return getDefaultLocale();
        }

        return Locale.ENGLISH;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException("Cannot change locale - use a different locale resolution strategy");
    }

}
