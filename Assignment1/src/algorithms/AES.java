/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author UNu
 */
public class AES {
    
    private static SecretKeySpec secretKey;

    public AES() {
    }
    
    public String getKey(){
        return (new BigInteger(this.secretKey.getEncoded())).toString(16);
    }

    public static void KeyAES(byte key[]) {

        secretKey = new SecretKeySpec(key, "AES");
        
        System.out.println(secretKey.getEncoded().length);
    }
    
    public static void KeyDES(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            
            String s = br.readLine();
            
            BigInteger bi = new BigInteger(s);
            
            secretKey = new SecretKeySpec(bi.toByteArray(), "AES");
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void saveKey(String path){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            
            BigInteger bi = new BigInteger(secretKey.getEncoded());
           
            bw.write(bi.toString());
            bw.flush();
            bw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } 
       
    }

    public static void encrypt(FileInputStream is, FileOutputStream os) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            CipherInputStream cis = new CipherInputStream(is, cipher);

            byte[] bytes = new byte[128];
            int numBytes;
            while ((numBytes = cis.read(bytes)) != -1) {
                os.write(bytes, 0, numBytes);
            }
            os.flush();
            os.close();
            is.close();
            cis.close();

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void decrypt(FileInputStream is, FileOutputStream os) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);

            byte[] bytes = new byte[128];
            int numBytes;
            while ((numBytes = is.read(bytes)) != -1) {
                cos.write(bytes, 0, numBytes);
            }
            cos.flush();
            cos.close();
            is.close();
            os.close();

        } catch (InvalidKeyException | IOException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}


