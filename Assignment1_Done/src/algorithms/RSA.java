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
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

/**
 *
 * @author ThanhNguyen
 */
public class RSA {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    
    public void Encrypt(File f, PublicKey k, String outFile){
        try {
            //Encryption
            FileInputStream fis = new FileInputStream(f.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(outFile);
            PublicKey secretKey = k;
            
            System.out.println(f.length());
            
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            byte[] buf = new byte[64];
            int read;
            while ((read=fis.read(buf))!= -1){
                cos.write(buf, 0, read);
            }
            //file.close();
            //outStream.flush();
            cos.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void Decrypt(File f, PrivateKey k, String outFile){
        try {
            //Encryption
            FileInputStream file = new FileInputStream(f.getAbsolutePath());
            FileOutputStream outStream = new FileOutputStream(outFile);
            
            PrivateKey secretKey = k;
            
            Cipher enc = Cipher.getInstance("RSA");
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
    
    public PublicKey getpublicKey(){
        return publicKey;
    }
    public PrivateKey getprivateKey(){
        return privateKey;
    }
    
    public void Keygenerator() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //SecureRandom random = new SecureRandom(); // cryptograph. secure random 
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }
    
    public void WritePublicKey(PublicKey key, String fileName) throws FileNotFoundException, IOException {
        FileOutputStream outStream = new FileOutputStream(fileName);
        ObjectOutputStream oout = new ObjectOutputStream(outStream);
        try {
          oout.writeObject(key);
        } finally {
          oout.close();
        }
    }
    public void WritePrivateKey(PrivateKey key, String fileName) throws FileNotFoundException, IOException {
        FileOutputStream outStream = new FileOutputStream(fileName);
        ObjectOutputStream oout = new ObjectOutputStream(outStream);
        try {
          oout.writeObject(key);
        } finally {
          oout.close();
        }
    }
    
    public PublicKey ReadPublicKey(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException{
        PublicKey key;
        FileInputStream file = new FileInputStream(fileName);
        ObjectInputStream oin = new ObjectInputStream(file);
        key = (PublicKey) oin.readObject();
        oin.close();
        return key;
    }
    public PrivateKey ReadPrivateKey(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException{
        PrivateKey key;
        FileInputStream file = new FileInputStream(fileName);
        ObjectInputStream oin = new ObjectInputStream(file);
        key = (PrivateKey) oin.readObject();
        oin.close();
        return key;
    }
    
}
