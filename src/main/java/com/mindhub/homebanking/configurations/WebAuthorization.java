package com.mindhub.homebanking.configurations;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

            http.authorizeRequests()
                    .antMatchers("/web/index.html", "/web/pages/public/**").permitAll()
                    .antMatchers("/web/scripts/login.js", "/web/scripts/register.js","/web/scripts/index.js","/web/scripts/theme.js","/web/styles/**","/web/images/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/login", "/api/clients", "/api/logout","/api/payment_point").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/clients/current/cards", "/api/clients/current/accounts", "/api/transactions", "/api/loans").hasAuthority("CLIENT")
                    .antMatchers(HttpMethod.GET,"/api/clients/current", "/api/accounts/{id}","/api/loans","/api/transactions_PDF").hasAuthority("CLIENT")
                    .antMatchers(HttpMethod.PATCH, "/api/clients/current/cards","/api/loans/payment").hasAuthority("CLIENT")
                    .antMatchers(HttpMethod.POST, "/api/admin/loans").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.GET, "/api/clients").hasAuthority("ADMIN")
                    .antMatchers("/web/pages/client/**","/web/scripts/accounts.js","/web/scripts/account.js","/web/scripts/cards.js",
                            "/web/scripts/create-cards.js","/web/scripts/transaction.js","/web/scripts/loan-application.js").hasAuthority("CLIENT")
                    .antMatchers("/web/pages/admin/**","/web/scripts/**").hasAuthority("ADMIN")
                    .anyRequest().denyAll()
            ;

            http.formLogin()
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .loginPage("/api/login");

            http.logout().logoutUrl("/api/logout");

            // turn off checking for CSRF tokens
            http.csrf().disable();

            //disabling frameOptions so h2-console can be accessed
            http.headers().frameOptions().disable();

            // if user is not authenticated, just send an authentication failure response
            http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

            // if login is successful, just clear the flags asking for authentication
            http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

            // if login fails, just send an authentication failure response
            http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

            // if logout is successful, just send a success response
            http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

            return http.build();

    }


    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }
}
