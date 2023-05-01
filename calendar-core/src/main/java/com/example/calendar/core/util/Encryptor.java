package com.example.calendar.core.util;

public interface Encryptor {

    String encrypt(String origin);

    boolean isMatch(String plain, String hashed);
}
