
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * Class for managing keys
 */
public class KeyManager {
    /* Path to keys */
    private String path;
    /* Identifier */
    private String identifier;
    /* Decryption key */
    private String decryptionKey;
    /* Encryption key */
    private String encryptionKey;
    /* Cipher used for encryption */
    private Cipher encryptor;
    /* Cipher used for decryption */
    private Cipher decryptor;

    /**
     * Create a new KeyManager for specified path
     */
    public KeyManager(String path) {
        this.path = path;
    }

    /**
     * Load key and identifier from files on a path specified
     */
    public void loadKeys() throws IOException {
        FileSystem fs = FileSystems.getDefault();
        Path identifierPath = fs.getPath(this.path, "identifier.txt"),
                encryptionKeyPath = fs.getPath(this.path, "encryption.key"),
                decryptionKeyPath = fs.getPath(this.path, "decryption.key");
        this.decryptionKey = new String(Files.readAllBytes(decryptionKeyPath)).trim();
        this.encryptionKey = new String(Files.readAllBytes(encryptionKeyPath)).trim();
        this.identifier = new String(Files.readAllBytes(identifierPath)).trim();
    }

    /**
     * Get the identifier loaded
     * @return identifier loaded
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Init encryptor and decryptor with initialization vector
     */
    public void setIV(String iv) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] ivBytes = new byte[16],
                encryptionKeyBytes = new byte[16],
                decryptionKeyBytes = new byte[16],
                ivDecBytes = decoder.decodeBuffer(iv),
                encryptionKeyDecBytes = this.encryptionKey.getBytes(),
                decryptionKeyDecBytes = this.decryptionKey.getBytes();

        System.arraycopy(ivDecBytes, 0, ivBytes, 0, ivDecBytes.length);
        System.arraycopy(encryptionKeyDecBytes, 0,
                encryptionKeyBytes, 0, encryptionKeyDecBytes.length);
        System.arraycopy(decryptionKeyDecBytes, 0,
                decryptionKeyBytes, 0, decryptionKeyDecBytes.length);

        // PKCS5Padding
        // NoPadding
        final String param = "AES/CBC/NoPadding";

        // Create encryptor
        this.encryptor = Cipher.getInstance(param);
        this.encryptor.init(Cipher.ENCRYPT_MODE,
                new SecretKeySpec(encryptionKeyBytes, "AES"),
                new IvParameterSpec(ivBytes));
        // Create decryptor
        this.decryptor = Cipher.getInstance(param);
        this.decryptor.init(Cipher.DECRYPT_MODE,
                new SecretKeySpec(decryptionKeyBytes, "AES"),
                new IvParameterSpec(ivBytes));
    }

    /**
     * Reencode request token for future usage
     */
    public String reencodeToken(String token) throws IOException, BadPaddingException, IllegalBlockSizeException {
        BASE64Decoder decoder = new BASE64Decoder();
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] tokenBytes = decoder.decodeBuffer(token),
                decryptedBytes = this.decryptor.doFinal(tokenBytes),
                encryptedBytes = this.encryptor.doFinal(decryptedBytes);
        return encoder.encodeBuffer(encryptedBytes);
    }
}
