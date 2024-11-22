package com.portifolio.wealthinker.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.portifolio.wealthinker.auth.dto.UserFormDTO;
import com.portifolio.wealthinker.user.models.User;
import com.portifolio.wealthinker.user.services.UserService;
import com.portifolio.wealthinker.utils.Message;
import com.portifolio.wealthinker.utils.MessageType;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {
    @Autowired 
    private UserService userService;

    @RequestMapping(value = {"","/","/home"})
    public String requestMethodName() {
        return "Home";
    }

    // Render the login page with potential error messages
    @RequestMapping("/login")
    public String loginPage(){
        
        return "auth/login"; // the login page template
    }

    // This is for registration page view
    @RequestMapping("/register")
    public String signupPage(Model model) {
        UserFormDTO userFormDTO = new UserFormDTO();
        // userForm.setName("VK");
        model.addAttribute("userFormDTO", userFormDTO);
        return "auth/register";
    }

    // processing registration form
    @RequestMapping(value="/process-register", method=RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserFormDTO userForm, BindingResult rBindingResult, HttpSession session) {
        
        // Check for existing username, email, or phone number
        if(userService.isUserExistByUsername(userForm.getUsername())){
            rBindingResult.rejectValue("username", "error.username", "Username is already taken. Please choose a different one.");
        }
        if(userService.isUserExistByEmail(userForm.getEmail())){
            rBindingResult.rejectValue("email", "error.email", "Email is already taken. Please choose a different one.");
        }
        if(userService.isUserExistByPhoneNumber(userForm.getPhoneNumber())){
            rBindingResult.rejectValue("phoneNumber", "error.phoneNumber", "Phone Number is already taken. Please choose a different one.");
        }
        if(rBindingResult.hasErrors()){
            return "auth/register";
        }


        User user = new User();
            user.setName(userForm.getName());
            user.setEmail(userForm.getEmail());
            user.setPassword(userForm.getPassword());
            user.setAbout(userForm.getAbout());
            user.setUsername(userForm.getUsername());
            user.setPhoneNumber(userForm.getPhoneNumber());
            // user.setProfilePic("https://www.google.com/url?sa=i&url=https%3A%2F%2Fstock.adobe.com%2Fsearch%3Fk%3Dpic&psig=AOvVaw3DqSCABBYScvwhLtkuYJND&ust=1729407144470000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCNDth-btmYkDFQAAAAAdAAAAABAO");
    
            user.setEnabled(true);
    
            User savedUser = userService.saveUser(user);
    
            System.out.println(savedUser);
    
            // message = "Registration Successfull"
    
            Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
    
            System.out.println("User Saved");
    
            session.setAttribute("message", message);
    
            // redirect
            return "redirect:/register";
    }
}
