package ir.FiveMFive.FiveMFive.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import ir.FiveMFive.FiveMFive.Java.User;

public class CredentialCrypter {
    private static final String TAG = "CredentialCrypter";
    private static final String ALIAS_CREDENTIAL = "credentialAlias";
    private static final String KEY_IV_USERNAME = "usernameIvKey";
    private static final String KEY_IV_PASSWORD = "passwordIvKey";
    private static final String KEY_USERNAME = "usernameKey";
    private static final String KEY_PASSWORD = "passwordKey";


    private final Context c;

    public CredentialCrypter(Context c) {
        this.c = c;
    }

    public void encrypt(User user) {

        KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(ALIAS_CREDENTIAL,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(false)
                .build();
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGen.init(spec);
            SecretKey secretKey = keyGen.generateKey();

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] usernameIv = generateIv();
            byte[] passwordIv = generateIv();
            byte[] encryptedUsername = cipherEncrypt(secretKey, usernameIv, user.getUsername());
            byte[] encryptedPassword = cipherEncrypt(secretKey, passwordIv, user.getPassword());

            SharedPreferences sp = c.getSharedPreferences(Constants.PREF_CREDENTIALS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString(KEY_USERNAME, byteToString(encryptedUsername));
            editor.putString(KEY_PASSWORD, byteToString(encryptedPassword));
            editor.putString(KEY_IV_USERNAME, byteToString(usernameIv));
            editor.putString(KEY_IV_PASSWORD, byteToString(passwordIv));
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public User decrypt() {

        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyStore.Entry entry = keyStore.getEntry(ALIAS_CREDENTIAL, null);
            SecretKey secretKey = ((KeyStore.SecretKeyEntry) entry).getSecretKey();

            SharedPreferences sp = c.getSharedPreferences(Constants.PREF_CREDENTIALS, Context.MODE_PRIVATE);

            byte[] usernameIv = stringToByte(sp.getString(KEY_IV_USERNAME, ""));
            byte[] passwordIv = stringToByte(sp.getString(KEY_IV_PASSWORD, ""));
            byte[] encryptedUsername = stringToByte(sp.getString(KEY_USERNAME, ""));
            byte[] encryptedPassword = stringToByte(sp.getString(KEY_PASSWORD, ""));



            Log.v(TAG, "username = " + new String(encryptedUsername));
            Log.v(TAG, "password = " + new String(encryptedPassword));

            if (usernameIv.length != 0 && passwordIv.length != 0) {

                String usernameValue = cipherDecrypt(secretKey, usernameIv, encryptedUsername);
                String passwordValue = cipherDecrypt(secretKey, passwordIv, encryptedPassword);

                User user = new User();
                user.setUsername(usernameValue);
                user.setPassword(passwordValue);
                return user;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private byte[] cipherEncrypt(SecretKey key, byte[] iv, String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private String cipherDecrypt(SecretKey key, byte[] iv, byte[] encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return new String(cipher.doFinal(encryptedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private byte[] generateIv() {
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }
    private String byteToString(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private byte[] stringToByte(String data) {
        return Base64.decode(data, Base64.DEFAULT);
    }

    public void removeSavedCredentials() {
        SharedPreferences sp = c.getSharedPreferences(Constants.PREF_CREDENTIALS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
