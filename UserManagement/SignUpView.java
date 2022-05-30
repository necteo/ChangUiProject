package UserManagement;

import SystemManagement.Client;
import SystemManagement.Protocol;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;

public class SignUpView extends JFrame implements ActionListener {  // 회원가입 화면 클래스

    private JRadioButton radMale;
    private JRadioButton radFemale;
    private int sex;
    private Client client;

    public SignUpView(Client c) {   // 생성자에서 기본 화면 생성
        setTitle("회원가입");
        setSize(280, 280);
        setResizable(false);
        setLocation(750, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        placeSignUpView(panel);
        add(panel);
        setVisible(true);

        client = c;
    }

    public void placeSignUpView(JPanel panel) { // 패널 구성
        panel.setLayout(null);
        JLabel lblID = new JLabel("*ID");
        lblID.setBounds(10, 10, 80, 25);
        panel.add(lblID);

        JTextField txtID = new JTextField(20);
        txtID.setBounds(80, 10, 180, 25);
        panel.add(txtID);

        JLabel lblPwd = new JLabel("*비밀번호");
        lblPwd.setBounds(10, 50, 80, 25);
        panel.add(lblPwd);

        JPasswordField txtPwd = new JPasswordField(20);
        txtPwd.setBounds(80, 50, 180, 25);
        panel.add(txtPwd);

        JLabel lblName = new JLabel("*이름");
        lblName.setBounds(10, 90, 80, 25);
        panel.add(lblName);

        JTextField txtName = new JTextField(20);
        txtName.setBounds(80, 90, 180, 25);
        panel.add(txtName);

        JLabel lblYear = new JLabel("*출생 연도");
        lblYear.setBounds(10, 130, 80, 25);
        panel.add(lblYear);

        JComboBox<Integer> cbxYear = new JComboBox<>();
        DefaultComboBoxModel cbm = new DefaultComboBoxModel();
        int firstYear = 1900;
        for (int i = firstYear; i <= LocalDate.now().getYear(); i++) {
            cbm.addElement(i);
        }
        cbxYear.setModel(cbm);
        cbxYear.setSelectedIndex(LocalDate.now().getYear() - firstYear - 20);
        cbxYear.setBounds(80, 130, 180, 25);
        panel.add(cbxYear);

//        JTextField txtYear = new JTextField();
//        txtYear.setBounds(80, 130, 180, 25);
//        panel.add(txtYear);

        JLabel lblSex = new JLabel("*성별");
        lblSex.setBounds(10, 170, 80, 25);
        panel.add(lblSex);

        ButtonGroup bgSex = new ButtonGroup();
        radMale = new JRadioButton("남자");
        radMale.setBounds(75, 170, 60, 25);
        bgSex.add(radMale);
        panel.add(radMale);
        radMale.addActionListener(this);

        radFemale = new JRadioButton("여자");
        radFemale.setBounds(140, 170, 60, 25);
        bgSex.add(radFemale);
        panel.add(radFemale);
        radFemale.addActionListener(this);

        JButton btnSignUp = new JButton("회원가입");
        btnSignUp.setBounds(10, 205, 245, 25);
        panel.add(btnSignUp);
        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread cw = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            synchronized (client) {
                                client.protocol = new Protocol(Protocol.PT_RES_SIGN_UP);
                                System.out.println("회원가입 정보 전송");
                                client.os.write(client.protocol.getPacket());
                            }
                            String id = txtID.getText();
                            String pwd = String.valueOf(txtPwd.getPassword());
                            String name = txtName.getText();
                            int year = (int) cbxYear.getSelectedItem();
                            UserDTO user = new UserDTO(id, pwd, name, year, sex);

                            ObjectOutputStream oos = new ObjectOutputStream(client.os);
                            oos.writeObject(user);
                            oos.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
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

                            if (packetType == Protocol.PT_SIGN_UP_RESULT) {
                                System.out.println("서버가 회원가입 결과 전송.");
                                String result = client.protocol.getSignUpResult();
                                if (result.equals("1")) {
                                    System.out.println("회원가입 성공");
                                    JOptionPane.showMessageDialog(null, "회원가입 성공");
                                    dispose();
                                } else if (result.equals("2")) {
                                    System.out.println("ID가 중복");
                                    JOptionPane.showMessageDialog(null, "이미 사용 중인 ID 입니다");
                                } else {
                                    System.out.println("알 수 없는 패킷");
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                cw.start();
                cr.start();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {    // 성별 선택 시 데이터 기억
        String s = e.getActionCommand();
        if (s.equals(radMale.getText()))
            sex = 0;
        else if (s.equals(radFemale.getText()))
            sex = 1;
    }
}
