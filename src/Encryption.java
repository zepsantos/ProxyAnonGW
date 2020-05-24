import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encryption {
    private static final byte[] tlsKey = new byte[] { 'O', 'G', 'R', 'U', 'P', 'O','0' ,'3', 'E', 'O', 'M', 'E','L', 'H', 'O', 'R', 'A', 'C', 'C', '2', '0','2','0','D' };

    // AES é um algoritmo de encriptaçao
    public static byte[] encrypt(byte[] Data) {

        Cipher c = null;
        try {
            KeyGenerator keygenerator = KeyGenerator.getInstance("AES/CBC/PKCS5Padding");
            SecretKey myDesKey = keygenerator.generateKey();
            Key key = new SecretKeySpec(tlsKey, "AES");
            c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, myDesKey);
            byte[] tmp =  c.doFinal(Data);
            return tmp;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return Data;


    }

    public static byte[] decrypt(byte[] encryptedData) {


        Key key = new SecretKeySpec(tlsKey, "AES/CBC/PKCS5Padding");
        Cipher c = null;
        try {
            KeyGenerator keygenerator = KeyGenerator.getInstance("AES/CBC/PKCS5Padding");
            SecretKey myDesKey = keygenerator.generateKey();
            c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, myDesKey);
            return c.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return encryptedData;


    }
}
