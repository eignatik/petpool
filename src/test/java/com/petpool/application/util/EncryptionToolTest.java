package com.petpool.application.util;

import org.testng.Assert;
import org.testng.annotations.Test;

public class EncryptionToolTest {

    private static final String ENCRYPTION_KEY = "QmFyMTIzNDVCYXIxMjM0NQ==";
    private EncryptionTool tool = new EncryptionTool("AES/CBC/PKCS5Padding", ENCRYPTION_KEY);

    @Test
    public void encryptionWorks() {
        final String source = "extraHardPass";
        String encrypted = tool.encrypt(source);
        Assert.assertNotNull(encrypted, "Tools should have encrypted the given text");
        System.out.println(encrypted);
        String decrypted = tool.decrypt(encrypted);
        Assert.assertEquals(decrypted, source);
    }

}
