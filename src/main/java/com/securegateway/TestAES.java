package com.securegateway;

import com.securegateway.util.AESUtil;

public class TestAES {

    public static void main(String[] args) {

        String message = "Hello Officer";

        String encrypted = AESUtil.encrypt(message);

        String decrypted = AESUtil.decrypt(encrypted);

        System.out.println("Original : " + message);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);

    }
}