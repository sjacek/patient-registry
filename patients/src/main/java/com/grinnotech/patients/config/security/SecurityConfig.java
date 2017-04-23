package com.grinnotech.patients.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import com.grinnotech.patients.config.AppProperties;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RememberMeServices rememberMeServices;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private Environment environment;

    @Override
    public void configure(WebSecurity web) throws Exception {
        if (environment.acceptsProfiles("development")) {
            web.ignoring().antMatchers("/resources/**", "/build/**", "/ext/**", "/**/*.js", "/bootstrap.json", "/robots.txt");
        } else {
            web.ignoring().antMatchers("/resources/**", "/app.js", "/app.json", "/i18n-de.js", "/i18n-en.js", "/i18n-pl.js", "/robots.txt");
        }
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder)
            throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                //.headers()
                //.frameOptions().sameOrigin()
                //  .and()
                .authorizeRequests()
                .antMatchers("/index.html", "/csrf", "/", "/router").permitAll()
                .antMatchers("/info", "/health").permitAll()
                .anyRequest().authenticated()
                .and()
                .rememberMe()
                .rememberMeServices(this.rememberMeServices)
                .key(this.appProperties.getRemembermeCookieKey())
                .and()
                .formLogin()
                .successHandler(this.authenticationSuccessHandler)
                .failureHandler(new JsonAuthFailureHandler())
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http401UnauthorizedEntryPoint());
        // @formatter:on
    }

}
