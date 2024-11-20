package com.portifolio.wealthinker.config;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.portifolio.wealthinker.auth.models.Providers;
import com.portifolio.wealthinker.exceptions.ResourceNotFoundException;
import com.portifolio.wealthinker.user.models.Role;
import com.portifolio.wealthinker.user.models.User;
import com.portifolio.wealthinker.user.repositories.RoleRepo;
import com.portifolio.wealthinker.user.repositories.UserRepo;
import com.portifolio.wealthinker.utils.AppConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

                // first we need to identify the provider from which user is login
                var oauth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
                String authorizedClientRegId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
               
                // user details which need to save in DB
                DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

                // checking which attributes comes from which authenticator
                // user.getAttributes().forEach((key, value) -> {
                //     System.out.println(key + " " + value);
                // });

                User user1 = new User();
                user1.setId(UUID.randomUUID().toString());
                user1.setEnabled(true);
                user1.setEmailVerified(true);
                Role defaultRole = roleRepo.findByName(AppConstants.ROLE_USER).orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
                user1.setRoles(Set.of(defaultRole));

                if(authorizedClientRegId.equalsIgnoreCase("google")){
                    //google attributes
                    String email = user.getAttribute("email").toString();
                    String name = user.getAttribute("name");
                    // String picture = user.getAttribute("picture").toString();
                    user1.setProviderUserId(user.getName());

                    // create user and save in DB            
                    user1.setEmail(email);
                    user1.setName(name);
                    // user1.setProfilePic(picture);
                    user1.setPassword("password");
                    user1.setAbout("This account is created using Google");
                    user1.setProvider(Providers.GOOGLE);

                }else if(authorizedClientRegId.equalsIgnoreCase("github")){
                    String email = user.getAttribute("email") != null ? user.getAttribute("email") : user.getAttribute("login").toString() + "@gmail.com";
                    // String picture = user.getAttribute("avatar_url").toString();
                    String name = user.getAttribute("login").toString();
                    String providerUserId = user.getName();

                    // create user and save in DB   
                    user1.setProviderUserId(providerUserId);         
                    user1.setEmail(email);
                    user1.setName(name);
                    // user1.setProfilePic(picture);
                    user1.setPassword("password");
                    user1.setAbout("This account is created using Github");
                    user1.setProvider(Providers.GITHUB);
                }

                User user2 = userRepo.findByEmail(user1.getEmail()).orElse(null);

                if(user2 == null){
                    userRepo.save(user1);
                }

                new DefaultRedirectStrategy().sendRedirect(request, response, "/home");
            }


}
