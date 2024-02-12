package io.crm.app.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

@Component
public class EncryptionAES {

    private EncryptionAES(){
    }

    private static final String ALGORITHM = "AES";

    private static SecretKey secretKey;

    static {
        try {

            byte[] decodedKey = Base64.getDecoder().decode("Cj92sv+b7dcdHoCHNOcU7w==");
            secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing SecretKey", e);
        }
    }

    public static String generateAndEncodeAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // You can change this to 192 or 256 for different key lengths
        SecretKey secretKey = keyGenerator.generateKey();

        byte[] encodedKey = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedKey);
    }

    public static String encryptId(String id) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(id.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptId(String encryptedId) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedId));
        return new String(decryptedBytes);
    }


}
