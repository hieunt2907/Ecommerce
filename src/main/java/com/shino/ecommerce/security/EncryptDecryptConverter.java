package com.shino.ecommerce.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
public class EncryptDecryptConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "1234567890123456"; // 16 ký tự cho AES-128

    private final Cipher encryptCipher;
    private final Cipher decryptCipher;

    public EncryptDecryptConverter() throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);

        encryptCipher = Cipher.getInstance(ALGORITHM);
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance(ALGORITHM);
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            if (attribute == null) return null;
            byte[] encrypted = encryptCipher.doFinal(attribute.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new IllegalStateException("Could not encrypt data", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) return null;
            byte[] decoded = Base64.getDecoder().decode(dbData);
            return new String(decryptCipher.doFinal(decoded));
        } catch (Exception e) {
            throw new IllegalStateException("Could not decrypt data", e);
        }
    }
}
