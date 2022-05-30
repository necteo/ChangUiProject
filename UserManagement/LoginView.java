package UserManagement;

import FoodNutrientManagement.FoodNtrView;
import SystemManagement.Client;
import SystemManagement.Protocol;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class LoginView extends JFrame implements ActionListener{     // 로그인 화면 클래스
    private JPasswordField txtPwd;
    private JTextField txtID;
    private Client client;

    public static void main(String[] args) {    // 시작점
        try {
            new LoginView();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LoginView() throws IOException {    // 생성자에서 기본 화면 생성
        setTitle("로그인");
        setSize(280, 150);
        setResizable(false);
        setLocation(700, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        placeLoginPanel(panel);
        add(panel);
        setVisible(true);

        client = new Client();
        client.conn();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    client.protocol = new Protocol(Protocol.PT_EXIT);
                    client.os.write(client.protocol.getPacket());
                    client.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                super.windowClosing(e);
            }
        });
    }

    public void placeLoginPanel(JPanel panel){  // 패널 구성
        panel.setLayout(null);
        JLabel lblID = new JLabel("ID");
        lblID.setBounds(10, 10, 80, 25);
        panel.add(lblID);

        JLabel lblPwd = new JLabel("비밀번호");
        lblPwd.setBounds(10, 40, 80, 25);
        panel.add(lblPwd);

        txtID = new JTextField(20);
        txtID.setBounds(80, 10, 180, 25);
        panel.add(txtID);

        txtPwd = new JPasswordField(20);
        txtPwd.setBounds(80, 40, 180, 25);
        panel.add(txtPwd);
        txtPwd.addActionListener(this);

        JButton btnLogin = new JButton("로그인");
        btnLogin.setBounds(135, 77, 120, 25);
        panel.add(btnLogin);
        btnLogin.addActionListener(this);

        JButton btnSignUp = new JButton("회원가입");
        btnSignUp.setBounds(10, 77, 120, 25);
        panel.add(btnSignUp);
        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUpView(client);
            }   // 회원가입 화면 출력
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Thread cw = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (client) {
                        client.protocol = new Protocol(Protocol.PT_RES_LOGIN);
                        client.protocol.setId(txtID.getText());
                        System.out.println(client.protocol.getId());
                        client.protocol.setPassword(String.valueOf(txtPwd.getPassword()));
                        System.out.println(client.protocol.getPassword());
                        System.out.println("로그인 정보 전송");
                        client.os.write(client.protocol.getPacket());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        Thread cr = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (client) {
                        client.protocol = new Protocol();
                    }
                    client.buf = client.protocol.getPacket();
                    client.is.read(client.buf);
                    int packetType = client.buf[0];
                    client.protocol.setPacket(packetType,client.buf);
                    if(packetType == Protocol.PT_EXIT){
                        System.out.println("클라이언트 종료");
                    }

                    if (packetType == Protocol.PT_LOGIN_RESULT) {
                        System.out.println("서버가 로그인 결과 전송.");
                        String result = client.protocol.getLoginResult();
                        if (result.equals("1")) {
                            System.out.println("로그인 성공");
                            new FoodNtrView(client, txtID.getText());
                            dispose();
                        } else if (result.equals("2")) {
                            System.out.println("아이디가 존재하지 않음");
                        } else {
                            System.out.println("알 수 없는 패킷");
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cw.start();
        cr.start();
    }
}