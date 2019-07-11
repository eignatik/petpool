package com.petpool.application.util;

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

/**
 * Provides with the encryption and decryption functionality.
 *
 * <p><strong>Note:</strong> never input the real either encrypted or decrypted encryption
 * encryptionKey in the application.
 * </p>
 */
public final class EncryptionTool {

  private static final String DEFAULT_KEY_ENC_ALGORITHM = "AES";

  private final String encryptionAlgorithm;
  private final String encryptionKey;

  /**
   * The only constructor that takes the encryption encryptionKey once for encryption and decryption
   * purposes.
   *
   * @param encryptionAlgorithm the algorithm to be used for cipher.
   * @param encryptionKey the key to be used for encryption and decryption.
   */
  public EncryptionTool(String encryptionAlgorithm, String encryptionKey) {
    this.encryptionAlgorithm = encryptionAlgorithm;
    this.encryptionKey = encryptionKey;
  }

  /**
   * Returns encrypted version of the given string.
   *
   * @param source the string to be encrypted.
   * @return encrypted string.
   */
  public String encrypt(String source) {
    String encrypted;
    try {
      Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
      cipher.init(Cipher.ENCRYPT_MODE, generateKey(), new IvParameterSpec(new byte[16]));
      byte[] enc = cipher.doFinal(source.getBytes());
      encrypted = Base64.getEncoder().encodeToString(enc);
    } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
        NoSuchPaddingException | InvalidAlgorithmParameterException e) {
      throw new SecurityException(e.getMessage(), e);
    }
    return encrypted;
  }

  /**
   * Returns decrypted value of given source.
   *
   * @param source string to be decrypted.
   * @return decrypted string.
   */
  public String decrypt(String source) {
    byte[] decrypted;
    try {
      Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
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
    String decodedKey = new String(Base64.getDecoder().decode(encryptionKey.getBytes()),
        StandardCharsets.UTF_8);
    return new SecretKeySpec(decodedKey.getBytes(), DEFAULT_KEY_ENC_ALGORITHM);
  }

}
