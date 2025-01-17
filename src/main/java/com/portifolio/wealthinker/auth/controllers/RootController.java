package com.portifolio.wealthinker.auth.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.portifolio.wealthinker.user.models.User;
import com.portifolio.wealthinker.user.services.UserService;
import com.portifolio.wealthinker.utils.Helper;

import jakarta.servlet.http.HttpSession;

// This will run for all requests

// @ControllerAdvice
// public class RootController {
//     private Logger logger = LoggerFactory.getLogger(this.getClass());

//     @Autowired
//     private UserService userservice;

//     // Model attribute is used for we get user information on all APIs of uesr
//     @ModelAttribute
//     public void addLoggedInUserInformation(Model model, Authentication authentication){
//         if(authentication == null) return;
//         logger.info("Adding model attribute");
//         String username = Helper.getEmailOfLoggedInUser(authentication);
        
//         logger.info("User Logged in: {}", username);
//         // fetchinfg data from db
//         User user = userservice.getUserByEmail(username);

//         logger.info("User is: {}", user);


//         // System.out.println("User name " + user.getName());
//         // System.out.println(user.getEmail());
//         // if user came then user add otheriwse null will add
//         model.addAttribute("loggedInUser", user);

//     }
// }


// This will run for all requests
@ControllerAdvice
@SessionAttributes("loggedInUser") // Make sure the user is saved in session
public class RootController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userservice;

    // Model attribute is used to add the logged-in user information to all APIs
    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication, HttpSession session) {
        if (authentication == null) {
            logger.info("No authentication found. User not logged in.");
            return;
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User Logged in: {}", username);

        // Check if the logged-in user is already in the session
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            // If user is not in session, fetch from DB and store in session
            user = userservice.getUserByEmail(username);
            logger.info("User fetched from DB: {}", user);

            // Store user in session
            session.setAttribute("loggedInUser", user);
        } else {
            logger.info("User found in session: {}", user);
        }

        model.addAttribute("loggedInUser", user);
    }
}