import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    private static final byte[] tlsKey = new byte[] { 'O', 'G', 'R', 'U', 'P', 'O','0' ,'3', 'E', 'O', 'M', 'E','L', 'H', 'O', 'R', 'A', 'C', 'C', '2', '0','2','0','D' };

    // AES é um algoritmo de encriptaçao
    public static byte[] encrypt(byte[] Data) {

        Cipher c = null;
        Key key = new SecretKeySpec(tlsKey, "AES");

        try {
            c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, key);
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


        Key key = new SecretKeySpec(tlsKey, "AES");
        Cipher c = null;
        try {
            c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, key);
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
