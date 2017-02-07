package org.thoughtworks.app.timeSheetTracker.decryption;


import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDecryption {

    @Test
    public void testDecryption() {
        final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("missingTimeSheetTracker");
        String encryptedText = encryptor.encrypt("MissingTimeSheetTracker");
        String plainText = Decryption.getDecryptedText(encryptedText);
        assertEquals("MissingTimeSheetTracker", plainText);
    }


}
