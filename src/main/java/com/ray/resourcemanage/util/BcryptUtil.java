package com.ray.resourcemanage.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BcryptUtil {
    public String bcryptEncode(String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashed;
    }
}
