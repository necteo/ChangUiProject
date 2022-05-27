package SystemManagement;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args){
        try {
            Socket s = new Socket("14.45.204.148", 8000);
            InetAddress ia = null; // Local Host IP Address 가져오기 위한 변수
            ia = InetAddress.getLocalHost();
            System.out.println(ia);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeUTF("클라이언트 연결");
            dos.flush();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
