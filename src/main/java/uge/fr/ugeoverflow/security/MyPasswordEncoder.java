package uge.fr.ugeoverflow.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MyPasswordEncoder extends BCryptPasswordEncoder {

    public MyPasswordEncoder(int strength) {
        super(strength);
    }

    public MyPasswordEncoder() {
        super(10);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return super.matches(rawPassword, encodedPassword);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return super.encode(rawPassword);
    }
}
