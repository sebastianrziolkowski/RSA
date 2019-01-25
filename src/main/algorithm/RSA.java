package main.algorithm;

import javax.crypto.Cipher;
import java.security.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;


import static java.nio.charset.StandardCharsets.UTF_8;

public class RSA {

    private KeyPair keyPair;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String message;
    private int keySize;
    private String cryptogram;


    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getCryptogram() {
        return cryptogram;
    }

    public void setCryptogram(String cryptogram) {
        this.cryptogram = cryptogram;
    }


    public RSA(String message, int keySize) throws NoSuchAlgorithmException {

        keyPair = buildKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

        this.message = message;
        this.keySize = keySize;

    }


    public KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048, new SecureRandom());
        return keyPairGenerator.genKeyPair();
    }

    public String encrypt() throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        cryptogram = Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(UTF_8)));
        return cryptogram;
    }


    public String decrypt() throws Exception {

        byte[] bytes = Base64.getDecoder().decode(cryptogram);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(cipher.doFinal(bytes), UTF_8);
    }


    // KEY MANIPULATE

    public String savePrivateKey() throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec spec = fact.getKeySpec(privateKey, PKCS8EncodedKeySpec.class);
        byte[] packed = spec.getEncoded();
        String key64 = Base64.getEncoder().encodeToString(packed);

        Arrays.fill(packed, (byte) 0);
        return key64;
    }

    public String savePublicKey() throws GeneralSecurityException {
        KeyFactory fact = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec = fact.getKeySpec(publicKey, X509EncodedKeySpec.class);
        return Base64.getEncoder().encodeToString(spec.getEncoded());
    }

    public void loadPrivateKey(String key64) throws GeneralSecurityException {
        byte[] clear = Base64.getDecoder().decode(key64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey newPrivateKey = fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        this.privateKey = newPrivateKey;
    }

}


