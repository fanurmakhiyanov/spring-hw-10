package ru.gb.util.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class CustomEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return String.valueOf(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}