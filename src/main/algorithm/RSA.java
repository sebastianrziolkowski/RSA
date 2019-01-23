package main.algorithm;

import javax.crypto.Cipher;
import java.security.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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

    public void setPrivateKey(String keyAsString) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyAsString));
        PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);
        this.privateKey = privKey;
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

        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(UTF_8)));
    }


    public String decrypt(String messageToDecrypt) throws Exception {

        byte[] bytes = Base64.getDecoder().decode(messageToDecrypt);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(cipher.doFinal(bytes), UTF_8);
    }

}


