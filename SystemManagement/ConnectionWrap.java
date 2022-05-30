package SystemManagement;

import FoodNutrientManagement.DailyNutrient;
import FoodNutrientManagement.NtrDataManager;
import UserManagement.UserDTO;
import UserManagement.UserInfoManager;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionWrap implements Runnable {
    private final Socket socket;

    public ConnectionWrap(Socket s) {
        socket = s;
    }

    @Override
    public void run() {
        System.out.println("스레드풀 생성: " + Thread.currentThread().getName());
        try {
            // 바이트 배열로 전송할 것이므로 필터 스트림 없이 Input/OutputStream 만 사용해도 됨
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            boolean program_stop = false;

            while (true) {
                Protocol protocol = new Protocol();            // 새 Protocol 객체 생성 (기본 생성자)
                byte[] buf = protocol.getPacket();    // 기본 생성자로 생성할 때에는 바이트 배열의 길이가 1000바이트로 지정됨
                System.out.println("입력대기");
                is.read(buf);                        // 클라이언트로부터 로그인정보 (ID와 PWD) 수신
                System.out.println("입력대기 종료");
                int packetType = buf[0];            // 수신 데이터에서 패킷 타입 얻음
                System.out.println(packetType);
                protocol.setPacket(packetType, buf);    // 패킷 타입을 Protocol 객체의 packet 멤버변수에 buf 를 복사
                UserInfoManager uim;
                NtrDataManager ndm;

                switch (packetType) {
                    case Protocol.PT_EXIT:            // 프로그램 종료 수신
                        protocol = new Protocol(Protocol.PT_EXIT);
                        os.write(protocol.getPacket());
                        program_stop = true;
                        System.out.println("서버종료");
                        break;

                    case Protocol.PT_RES_LOGIN:        // 로그인 정보 수신
                        System.out.println("클라이언트가 로그인 정보를 보냈습니다");
                        String id = protocol.getId();
                        String password = protocol.getPassword();
                        System.out.println(id + password);

                        uim = new UserInfoManager();
                        String info = uim.getInfo(id, password);

                        if (isLoginValid(info, id, password)) {
                            uim.controlLoginState(id, 0);  // 로그인 상태가 아님
                            protocol = new Protocol(Protocol.PT_LOGIN_RESULT);
                            protocol.setLoginResult("1");
                            System.out.println("로그인 성공");
                        } else {    //아이디 존재 안함
                            protocol = new Protocol(Protocol.PT_LOGIN_RESULT);
                            protocol.setLoginResult("2");
                            System.out.println("로그인 실패");
                        }

                        System.out.println("로그인 처리 결과 전송");
                        os.write(protocol.getPacket());
                        break;
                    case Protocol.PT_RES_SIGN_UP:
                        ObjectInputStream ois = new ObjectInputStream(is);
                        System.out.println("클라이언트가 회원가입 정보를 보냈습니다");
                        UserDTO user = (UserDTO) ois.readObject();
                        System.out.println(user.getId());

                        uim = new UserInfoManager();
                        String i = user.getId();
                        String p = user.getPassword();
                        String n = user.getName();
                        int y = user.getYear();
                        int s = user.getSex();
                        if (uim.setInfo(i, p, n, y, s)) {
                            protocol = new Protocol(Protocol.PT_SIGN_UP_RESULT);
                            protocol.setSignUpResult("1");
                            System.out.println("회원가입 성공");
                        } else {
                            protocol = new Protocol(Protocol.PT_SIGN_UP_RESULT);
                            protocol.setSignUpResult("2");
                            System.out.println("ID 중복");
                        }
                        System.out.println("회원가입 처리 결과 전송");
                        os.write(protocol.getPacket());
                        break;
                    case Protocol.PT_RES_DAILY_NUTR:
                        ois = new ObjectInputStream(is);
                        System.out.println("클라이언트가 영양소 정보를 보냈습니다");
                        DailyNutrient dn = (DailyNutrient) ois.readObject();
                        ndm = new NtrDataManager();
                        ndm.insertData(dn);
                        System.out.println("저장완료");
                        break;
                    case Protocol.PT_RES_CHART_DATE:
                        System.out.println("클라이언트가 통계 표시 날짜를 보냈습니다");
                        String[] date = protocol.getDate().split(",");
                        int[] startDate = new int[]{Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])};
                        int[] endDate = new int[]{Integer.parseInt(date[3]), Integer.parseInt(date[4]), Integer.parseInt(date[5])};
                        ndm = new NtrDataManager();
                        ArrayList<DailyNutrient> dnList = ndm.readData(startDate, endDate);
                        ObjectOutputStream oos = new ObjectOutputStream(os);
                        oos.writeObject(dnList);
                        System.out.println("서버가 영양소 정보를 보냈습니다");
                        oos.flush();
                        break;
                }//end switch
                if (program_stop) break;

            }//end while

            is.close();
            os.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isLoginValid(String info, String i, String p){
        if (info.equals(",")) {   // 해당하는 계정이 없는 경우
            return false;
        }

        String id = info.split(",")[0];
        String pwd = info.split(",")[1];
        // id와 비밀번호가 불일치
        return i.equals(id) && p.equals(pwd);
    }
}
