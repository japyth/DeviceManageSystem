package com.ray.resourcemanage.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BcryptUtil {
    public static void main(String[] args) {
        String password = "111";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(hashed);
    }
}
