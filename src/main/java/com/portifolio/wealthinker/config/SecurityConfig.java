package com.portifolio.wealthinker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.portifolio.wealthinker.user.servicesImpl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {
    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

     @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // user detail service's object 
        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        // password encoder's object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }
    @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/signup").permitAll();
            // authorize.requestMatchers("/logout").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        // It is for default login page which provided by spring security
        // httpSecurity.formLogin(Customizer.withDefaults());

        // It is redirect to our login page which made by us
        httpSecurity.formLogin(formLogin -> {
            // is url ka page khol dega
            formLogin.loginPage("/login");

            // form submit karne par jo url chlega
            formLogin.loginProcessingUrl("/authenticate");

            // success hone par jha jayega
            // formLogin.successForwardUrl("/user/dashboard");

            // this will always redirect to dashboard page
            //formLogin.defaultSuccessUrl("/user/dashboard", true);
            
            // this will redirect where user want to redirect after login
            formLogin.defaultSuccessUrl("/home");

            // fail hone par jo chlega
            formLogin.failureForwardUrl("/login?error=true");

            // form me jo field username rhega wo
            formLogin.usernameParameter("email");

            formLogin.passwordParameter("password");

            formLogin.failureHandler(authFailureHandler);

        });
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logout -> {
            logout.logoutUrl("/logout");  // This is the URL that handles logout (expects POST)
            logout.logoutSuccessUrl("/login?logout=true");  // Redirect to login after logout
            logout.invalidateHttpSession(true); // Invalidate session on logout
            logout.clearAuthentication(true);  // Clear authentication on logout
            logout.deleteCookies("JSESSIONID");  // Delete session cookies
        });

        // oauth configurations i.e. login with google or github etc.

        // This is for default google, github login whatever in application.properties file
        // httpSecurity.oauth2Login(Customizer.withDefaults());

        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });

        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}