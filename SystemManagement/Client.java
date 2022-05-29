package SystemManagement;

import java.io.*;
import java.net.*;

public class Client {
    public Socket socket;
    public OutputStream os;
    public InputStream is;
    public Protocol protocol;
    public byte[] buf;

    public void conn() throws IOException {
        socket = new Socket("", 8000);
        os = socket.getOutputStream();
        is = socket.getInputStream();
        protocol = new Protocol();
        buf = protocol.getPacket();
    }

    public void close() throws IOException {
        os.close();
        is.close();
        socket.close();
    }

}
