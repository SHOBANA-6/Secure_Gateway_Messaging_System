package com.securegateway.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {

    // 256-bit key (32 bytes)
    private static final String SECRET_KEY =
            "SecureGatewayAESKey2026Project!!";

    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    private static SecretKey getKey() {

        return new SecretKeySpec(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8),
                "AES"
        );
    }

    /**
     * Encrypt text using AES-GCM
     */
    public static String encrypt(String plainText) {

        try {

            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            GCMParameterSpec spec =
                    new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            cipher.init(Cipher.ENCRYPT_MODE, getKey(), spec);

            byte[] encrypted =
                    cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] encryptedIVAndText =
                    new byte[iv.length + encrypted.length];

            System.arraycopy(iv, 0,
                    encryptedIVAndText, 0,
                    iv.length);

            System.arraycopy(encrypted, 0,
                    encryptedIVAndText,
                    iv.length,
                    encrypted.length);

            return Base64.getEncoder()
                    .encodeToString(encryptedIVAndText);

        } catch (Exception e) {

            throw new RuntimeException("Encryption failed", e);

        }

    }

    /**
     * Decrypt text using AES-GCM
     */
    public static String decrypt(String encryptedText) {

        try {

            byte[] decoded =
                    Base64.getDecoder().decode(encryptedText);

            byte[] iv = new byte[GCM_IV_LENGTH];

            System.arraycopy(decoded,
                    0,
                    iv,
                    0,
                    iv.length);

            byte[] cipherText =
                    new byte[decoded.length - iv.length];

            System.arraycopy(decoded,
                    iv.length,
                    cipherText,
                    0,
                    cipherText.length);

            Cipher cipher =
                    Cipher.getInstance("AES/GCM/NoPadding");

            GCMParameterSpec spec =
                    new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            cipher.init(Cipher.DECRYPT_MODE, getKey(), spec);

            byte[] decrypted =
                    cipher.doFinal(cipherText);

            return new String(decrypted,
                    StandardCharsets.UTF_8);

        } catch (Exception e) {

            throw new RuntimeException("Decryption failed", e);

        }

    }

}