package main.algorithm;


import javax.crypto.Cipher;
import java.security.*;

public class RSA {

    private KeyPair keyPair;


    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private PublicKey publicKey;

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    private PrivateKey privateKey;
    private String message;
    private int keySize;

    public RSA(String message, int keySize) throws NoSuchAlgorithmException {

        keyPair = buildKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

        this.message = message;
        this.keySize = keySize;

    }


    public KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.genKeyPair();
    }

    public byte[] encrypt() throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(message.getBytes());
    }


    public byte[] decrypt(byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(encrypted);
    }

}


