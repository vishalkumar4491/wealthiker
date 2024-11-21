package com.portifolio.wealthinker.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.portifolio.wealthinker.user.models.User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication){

        // checking from which user is logged in i.e. email, gmail or github
        if(authentication instanceof OAuth2AuthenticationToken){
            // it means user is logged in via gmail or github

            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oAuth2User = (OAuth2User) authentication.getPrincipal();

            String username = "";

            if(clientId.equalsIgnoreCase("github")){
                username = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email") : oAuth2User.getAttribute("login").toString() + "@gmail.com";
            }else{
                username = oAuth2User.getAttribute("email").toString();
            }
            return username;  
        }else{
            // it means user logged in with gmail and password

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Assuming the principal is a User object, which has an email field
            if (userDetails instanceof User) {
                User user = (User) userDetails;
                return user.getEmail();  // Directly return the email
            }
        }
        return null;
    }

}
