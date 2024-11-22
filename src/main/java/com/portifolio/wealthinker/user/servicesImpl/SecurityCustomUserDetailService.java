package com.portifolio.wealthinker.user.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.portifolio.wealthinker.auth.models.Providers;
import com.portifolio.wealthinker.exceptions.OAuthLoginException;
import com.portifolio.wealthinker.user.models.User;
import com.portifolio.wealthinker.user.repositories.UserRepo;

@Service
public class SecurityCustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        User user = null;

        // Check if input is an email address (contains '@')
        if (input.contains("@")) {
            user = userRepository.findByEmail(input)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + input));

            // Check if the user logged in with Google (OAuth)
            if (user.getProvider() == Providers.GOOGLE) {
                throw new OAuthLoginException("This account was created with Google. Please login using Google.");
            }
        }
        // Check if input is a phone number (only digits, or optionally starts with +)
        else if (input.matches("^\\+?\\d+$")) {
            user = userRepository.findByPhoneNumber(input)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + input));
        }
        // Else assume it's a username (alphanumeric, starts with a letter)
        else if (isValidUsername(input)) {
            user = userRepository.findByUsername(input)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + input));
        }

        // If the user is null after all checks, throw exception
        if (user == null) {
            throw new UsernameNotFoundException("User not found with input: " + input);
        }

        return user;

    }
    private boolean isValidUsername(String username) {
        // Ensure username starts with a letter and contains only alphanumeric characters
        return username.matches("^[a-zA-Z][a-zA-Z0-9]*$");
    }
}
