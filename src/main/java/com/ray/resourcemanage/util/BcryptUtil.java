package com.ray.resourcemanage.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BcryptUtil {
    public static String bcryptEncode(String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashed;
    }

    public static Boolean checkBcypt(String password, String hashPw) {
        return BCrypt.checkpw(password, hashPw);
    }

    /*public static void main(String[] args) {
        System.out.println(bcryptEncode("123"));
    }*/
}
