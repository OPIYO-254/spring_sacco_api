package com.sojrel.saccoapi.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGeneratorUtility {
    public static KeyPair generateRsaKey(){
        KeyPair keyPair;
        KeyPairGenerator keyPairGenerator;

        {
            try {
                keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                keyPair = keyPairGenerator.generateKeyPair();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return keyPair;
    }
}
