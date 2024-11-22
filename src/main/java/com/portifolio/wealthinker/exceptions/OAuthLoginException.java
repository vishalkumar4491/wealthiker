package com.portifolio.wealthinker.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class OAuthLoginException extends UsernameNotFoundException {
    public OAuthLoginException(String message) {
        super(message);
    }
}
