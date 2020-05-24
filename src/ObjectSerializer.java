import java.io.*;

public class ObjectSerializer {

    public static byte[] getObjectInByte(Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(500);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            // get the byte array of the object
            byte[] obj= byteArrayOutputStream.toByteArray();
            byte[] encryptedObj = Encryption.encrypt(obj);
            byteArrayOutputStream.close();
            return encryptedObj;
        }catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static Object getObjectFromByte(byte[] bytes) {
        try {
            byte[] decryptedBuf = Encryption.decrypt(bytes);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBuf);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object tmp =  objectInputStream.readObject();
            byteArrayInputStream.close();
            return tmp;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}