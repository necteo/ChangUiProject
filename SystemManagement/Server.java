package SystemManagement;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args){
        try {
            ServerSocket ss = new ServerSocket(8000);
            Socket s = ss.accept();
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String str = dis.readUTF();
            System.out.println("message: " + str);
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
