package com.petpool.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class EncryptionTool {

    private static final String ALGORITHM = "AES";

    private final String algorithm;
    private final String key;

    public EncryptionTool(String algorithm, String key) {
        this.algorithm = algorithm;
        this.key = key;
    }

    public String encrypt(String source) {
        String encrypted;
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(), new IvParameterSpec(new byte[16]));
            byte[] enc = cipher.doFinal(source.getBytes());
            encrypted = Base64.getEncoder().encodeToString(enc);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            throw new SecurityException(e.getMessage(), e);
        }
        return encrypted;
    }

    public String decrypt(String source) {
        byte[] decrypted;
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, generateKey(), new IvParameterSpec(new byte[16]));
            byte[] decryptedValue = Base64.getDecoder().decode(source.getBytes());
            decrypted = cipher.doFinal(decryptedValue);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            throw new SecurityException(e.getMessage(), e);
        }
        return new String(decrypted);
    }

    private Key generateKey() {
        String decodedKey = new String(Base64.getDecoder().decode(key.getBytes()), StandardCharsets.UTF_8);
        return new SecretKeySpec(decodedKey.getBytes(), ALGORITHM);
    }

}
