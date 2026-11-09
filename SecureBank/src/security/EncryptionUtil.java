package security;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class EncryptionUtil {
    private static final String AES = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH_BITS = 128;

    /*
     * Classroom demo key only. For a real application, load a randomly generated
     * 32-byte key from a protected environment variable or secret manager.
     * Never commit production encryption keys to source control.
     */
    private static final byte[] DEMO_KEY =
            "SecureBank-Demo-Key-32-Bytes!!!!".getBytes(StandardCharsets.UTF_8);

    private EncryptionUtil() {}

    private static SecretKeySpec getKey() {
        String encoded = System.getenv("SECUREBANK_KEY_BASE64");
        byte[] keyBytes = (encoded == null || encoded.isBlank())
                ? DEMO_KEY
                : Base64.getDecoder().decode(encoded);

        if (keyBytes.length != 32) {
            throw new IllegalStateException(
                    "SECUREBANK_KEY_BASE64 must decode to exactly 32 bytes.");
        }
        return new SecretKeySpec(keyBytes, AES);
    }

    public static String encrypt(String plainText) throws Exception {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getKey(),
                new GCMParameterSpec(TAG_LENGTH_BITS, iv));

        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);
        buffer.put(iv);
        buffer.put(encrypted);
        return Base64.getEncoder().encodeToString(buffer.array());
    }

    public static String decrypt(String encryptedText) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedText);
        if (combined.length <= IV_LENGTH) {
            throw new IllegalArgumentException("Invalid encrypted data.");
        }

        ByteBuffer buffer = ByteBuffer.wrap(combined);
        byte[] iv = new byte[IV_LENGTH];
        buffer.get(iv);
        byte[] encrypted = new byte[buffer.remaining()];
        buffer.get(encrypted);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, getKey(),
                new GCMParameterSpec(TAG_LENGTH_BITS, iv));

        return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
    }

    // SHA-256 digest used by this classroom demo to verify the retrieved fields.
    public static String sha256(String value) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyHash(String value, String expectedHash) throws Exception {
        byte[] actual = Base64.getDecoder().decode(sha256(value));
        byte[] expected = Base64.getDecoder().decode(expectedHash);
        return MessageDigest.isEqual(actual, expected);
    }
}
