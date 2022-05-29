package SystemManagement;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    private final static int THREAD_CNT = 4;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_CNT);

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8000);
            System.out.println("클라이언트 접속 대기중...");
            while (true) {
                Socket socket = ss.accept();
                System.out.println("클라이언트 접속");
                threadPool.submit(new ConnectionWrap(socket));
                ThreadPoolExecutor tpe = (ThreadPoolExecutor) threadPool;
                System.out.println("총 개수:" + tpe.getActiveCount());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
    }
}
