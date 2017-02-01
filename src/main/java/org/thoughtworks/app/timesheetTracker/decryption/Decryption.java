package org.thoughtworks.app.timesheetTracker.decryption;


import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class Decryption {

    public static String getDecryptedText(String textToDecrypt) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(System.getenv("TIMESHEET_TRACKER_KEY"));
        return encryptor.decrypt(textToDecrypt);
    }
}
