package com.example.calendar.core.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptEncryptor implements Encryptor {

    @Override
    public String encrypt(String origin) {
        return BCrypt.hashpw(origin, BCrypt.gensalt());
    }

    @Override
    public boolean isMatch(String plain, String hashed) {
        return BCrypt.checkpw(plain, hashed);
    }
}
