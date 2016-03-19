package files;

import algorithms.DES;
import algorithms.RSA;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

public class FileServer extends Thread {
        private final static String MY_RSA_PRIVATE_KEY = "RSAPrivateSendFile.txt";
        private final static String MY_RSA_PUBLIC_KEY = "RSAPublicSendFile.txt";
        private final static String MY_DES_KEY = "DESKeySendFile.txt";
        private final static String RSA_PUBLIC_KEY_SERVER = "RSAPublicSendFile.txt";
        private final static String DES_KEY_SERVER = "DESKeySendFile.txt";
        private final static String DES_KEY_SERVER_ENCRYPTED = "DESKeySendFileEncypted.txt";
	private final static String RSA_PUBLIC_KEY_CLIENT = "RSAclient.txt"; // protocol receive
        private final static String SERVER_KEY = "serverkey.txt"; // protocol receive
        
        
        
	private ServerSocket ss;
	
	public FileServer(int port) {
		try {
			ss = new ServerSocket(port);
                        System.out.println(ss.getInetAddress().getHostAddress());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (true) {
			try {
				Socket clientSock = ss.accept();
				saveFile(clientSock);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveFile(Socket clientSock) throws IOException {
		DataInputStream dis = new DataInputStream(clientSock.getInputStream());
                String fileName= JOptionPane.showInputDialog("Please input your file name: ");
                if(RSA_PUBLIC_KEY_CLIENT.equals(fileName)){
                    try {
                        //receive file
                        FileOutputStream fos = new FileOutputStream(fileName);
                        
                        byte[] buf = new byte[1];
                        int read;
                        while ((read=dis.read(buf))!= -1){
                            fos.write(buf, 0, read);
                        }
//                        byte[] buffer = new byte[4096];
//                        
//                        int filesize = 15123; // Send file size in separate msg
//                        int read = 0;
//                        int totalRead = 0;
//                        int remaining = filesize;
//                        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
//                            totalRead += read;
//                            remaining -= read;
//                            System.out.println("read " + totalRead + " bytes.");
//                            fos.write(buffer, 0, read);
//                        }
                        
                        fos.close();
                        dis.close();
                        
                        //send symetric key
                        System.out.println(clientSock.getLocalAddress().toString());
                        System.out.println(clientSock.getPort());
                        RSA rsa = new RSA();
                        File f = new File(MY_DES_KEY);
                        rsa.Encrypt(f,rsa.ReadPublicKey(RSA_PUBLIC_KEY_CLIENT), DES_KEY_SERVER_ENCRYPTED );
                        String clientIP= JOptionPane.showInputDialog("Please input server's client IP: ");
                        String clientPort= JOptionPane.showInputDialog("Please input server's client port: ");
                        FileClient fc = new FileClient(clientIP, Integer.valueOf(clientPort), DES_KEY_SERVER_ENCRYPTED);
                        
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if(SERVER_KEY.equals(fileName)){
                    FileOutputStream fos = new FileOutputStream(fileName);
                    byte[] buf = new byte[1];
                    int read;
                    while ((read=dis.read(buf))!= -1){
                        fos.write(buf, 0, read);
                    }
//                    byte[] buffer = new byte[4096];
//                       
//                    int filesize = 15123; // Send file size in separate msg
//                    int read = 0;
//                    int totalRead = 0;
//                    int remaining = filesize;
//                    while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
//                            totalRead += read;
//                            remaining -= read;
//                            System.out.println("read " + totalRead + " bytes.");
//                            fos.write(buffer, 0, read);
//                    }

                    fos.close();
                    dis.close();
                }
                else{
                    FileOutputStream fos = new FileOutputStream(fileName);
                    byte[] buf = new byte[1];
                    int read;
                    while ((read=dis.read(buf))!= -1){
                        fos.write(buf, 0, read);
                    }
//                    byte[] buffer = new byte[4096];
//
//                    int filesize = 15123; // Send file size in separate msg
//                    int read = 0;
//                    int totalRead = 0;
//                    int remaining = filesize;
//                    while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
//                            totalRead += read;
//                            remaining -= read;
//                            System.out.println("read " + totalRead + " bytes.");
//                            fos.write(buffer, 0, read);
//                    }

                    fos.close();
                    dis.close();
                    
                    DES des = new DES();
                    SecretKey key;
                    key = des.ReadKeyEncode(MY_DES_KEY);
                    File f = new File(fileName);
                    des.Decrypt(f, key, "Received"+fileName);
                }
	}
	
	//public static void main(String[] args) {
	//	FileServer fs = new FileServer(1988);
	//	fs.start();
	//}

}   