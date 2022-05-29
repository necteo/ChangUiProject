package SystemManagement;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ActionThreads implements Runnable {
    Thread sin, sout;
    Thread cin, cout;
    Socket socket;
    InputStream is;
    OutputStream os;
    Client client;

    public ActionThreads(Socket s) throws IOException {
        socket = s;
        is = socket.getInputStream();
        os = socket.getOutputStream();

        sin = new Thread(this);
        sout = new Thread(this);
    }

    public ActionThreads(Client c) {
        client = c;

        cin = new Thread(this);
        cout = new Thread(this);
    }

    @Override
    public void run() {
        try {
            if (Thread.currentThread() == cin) {
                
            } else if (Thread.currentThread() == cout) {
                System.out.println("로그인 정보 전송");
                client.os.write(client.protocol.getPacket());
            } else if (Thread.currentThread() == sin) {
                
            } else if (Thread.currentThread() == sout) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
