/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author ThanhNguyen
 */
public class DES {
    public void Encrypt(File f, SecretKey k, String outFile){
        try {
            //Encryption
            FileInputStream file = new FileInputStream(f.getAbsolutePath());
            FileOutputStream outStream = new FileOutputStream(outFile);
            
//            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
//            SecureRandom random = new SecureRandom(); // cryptograph. secure random 
//            keyGen.init(random); 
            SecretKey secretKey = k;
            
            //byte k[] = new byte[16];
            //k = "vthanhnguyenducvanjvthanhnguyenducvanjvthanhnguyenducvanjvthanhnguyenducvanjvthanhnguyenducvanjvthanhnguyenducvanjvthanhnguyenducvanj".getBytes();
           // SecretKeySpec  key = new SecretKeySpec(k, "AES");
           
            //System.out.println(secretKey.getEncoded());
            Cipher enc = Cipher.getInstance("DES");
            enc.init(Cipher.ENCRYPT_MODE, secretKey);
            CipherOutputStream cos = new CipherOutputStream(outStream, enc);
            byte[] buf = new byte[64];
            int read;
            while ((read=file.read(buf))!= -1){
                cos.write(buf, 0, read);
            }
            file.close();
            outStream.flush();
            cos.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    public void Decrypt(File f, SecretKey k, String outFile){
        try {
            //Encryption
            FileInputStream file = new FileInputStream(f.getAbsolutePath());
            FileOutputStream outStream = new FileOutputStream(outFile);
            
//            KeyGenerator keyGen = KeyGenerator.getInstance("DES");
//            SecureRandom random = new SecureRandom(); // cryptograph. secure random 
//            keyGen.init(random); 
            SecretKey secretKey = k;
            
            //byte k[] = new byte[16];
            //k = "vthanhnguyenducvanjvthanhnguyenducvanjvthanhnguyenducvanjvthanhnguyenducvanjvthanhnguyenducvanjvthanhnguyenducvanjvthanhnguyenducvanj".getBytes();
           // SecretKeySpec  key = new SecretKeySpec(k, "AES");
           
            //System.out.println(secretKey.getEncoded());
            Cipher enc = Cipher.getInstance("DES");
            enc.init(Cipher.DECRYPT_MODE, secretKey);
            CipherOutputStream cos = new CipherOutputStream(outStream, enc);
            byte[] buf = new byte[64];
            int read;
            while ((read=file.read(buf))!= -1){
                cos.write(buf, 0, read);
            }
            file.close();
            outStream.flush();
            cos.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public SecretKey Keygenerator() throws NoSuchAlgorithmException{
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        SecureRandom random = new SecureRandom(); // cryptograph. secure random 
        keyGen.init(random); 
        SecretKey secretKey = keyGen.generateKey();
        return secretKey;
    }
    public void WriteKeyEncode(SecretKey key, String fileName) throws FileNotFoundException, UnsupportedEncodingException{
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        PrintWriter writer = new PrintWriter(fileName);
        writer.print(encodedKey);
        writer.close();
    }
    public SecretKey ReadKeyEncode(String filename) throws FileNotFoundException, IOException{
        SecretKey key = null;
        String encodedKey = new String(Files.readAllBytes(Paths.get(filename)));
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES"); 
        return key;
    }
    public void WriteKey(SecretKey key, String fileName) throws FileNotFoundException, IOException {
        FileOutputStream outStream = new FileOutputStream(fileName);
        ObjectOutputStream oout = new ObjectOutputStream(outStream);
        try {
          oout.writeObject(key);
        } finally {
          oout.close();
        }
    }
    
    public SecretKey ReadKey(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException{
        SecretKey key;
        FileInputStream file = new FileInputStream(fileName);
        ObjectInputStream oin = new ObjectInputStream(file);
        key = (SecretKey) oin.readObject();
        oin.close();
        return key;
    }
    
}
