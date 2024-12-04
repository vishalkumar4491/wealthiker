package com.portifolio.wealthinker.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.portifolio.wealthinker.auth.dto.UserFormDTO;
import com.portifolio.wealthinker.exceptions.ResourceNotFoundException;
import com.portifolio.wealthinker.user.models.User;
import com.portifolio.wealthinker.user.services.UserService;

import jakarta.validation.Valid;



@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private PasswordEncoder passwordEncoder;



    @Autowired
    private UserService userService;

    @GetMapping("/api/{id}")
    @ResponseBody
    public User getUserDetails(@PathVariable String id){
        return userService.getUserById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with ID : "+ id));
    }

    // user profile page
    @GetMapping("/profile")
    public String getUserProfile(@ModelAttribute("loggedInUser") User loggedInUser, Model model){
        if (loggedInUser == null) {
        return "redirect:/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);
        return "user/user_profile";
    }

    // update user form view
    @RequestMapping("/view/{id}")
    public String updateUserFormView(@PathVariable String id, Model model){
        var user = userService.getUserById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserFormDTO userFormDTO = new UserFormDTO();
        userFormDTO.setName(user.getName());
        userFormDTO.setEmail(user.getEmail());
        userFormDTO.setUsername(user.getUsername());
        userFormDTO.setAbout(user.getAbout());
        userFormDTO.setPhoneNumber(user.getPhoneNumber());
        userFormDTO.setPassword(user.getPassword());

        model.addAttribute("userFormDTO", userFormDTO);
        model.addAttribute("id", id);

        return "user/update_user";
    }

    // update user details
    @RequestMapping(value="/update/{id}", method=RequestMethod.POST)
    public String updateUser(@PathVariable String id, @Valid @ModelAttribute UserFormDTO userFormDTO,BindingResult result, Model model){
        // Validate the form
        System.out.println("Updated user0 is " + userFormDTO.toString());
        // System.out.println("Updated user Password is is " + user2.getPassword());

        var user2 = userService.getUserById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        System.out.println("Updated user0  pass is " + user2.getPassword());


        // if (result.hasErrors()) {
        //     return "user/update_user";
        // }

        System.out.println("Updated user2 is " + userFormDTO.toString());



        System.out.println("Updated user3 is " + userFormDTO.toString());
        System.out.println("Updated user4 is " + user2.toString());



        user2.setName(userFormDTO.getName());
        user2.setEmail(userFormDTO.getEmail());
        user2.setUsername(userFormDTO.getUsername());
        user2.setAbout(userFormDTO.getAbout());
        user2.setPhoneNumber(userFormDTO.getPhoneNumber());
        // user2.setPassword(userFormDTO.getPassword());

        // user2.setPassword(passwordEncoder.encode(user2.getPassword()));


        System.out.println("Updated user2 is " + user2.toString());


        userService.updateUser(user2);

        return "redirect:/home";

    }

    // delete user permanently
    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/home";
    }

}
