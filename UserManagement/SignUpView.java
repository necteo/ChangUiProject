package UserManagement;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpView extends JFrame implements ActionListener {  // 회원가입 화면 클래스

    private JRadioButton radMale;
    private JRadioButton radFemale;
    private int sex;

    public SignUpView() {   // 생성자에서 기본 화면 생성
        setTitle("회원가입");
        setSize(280, 240);
        setResizable(false);
        setLocation(750, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        placeSignUpView(panel);
        add(panel);
        setVisible(true);
    }

    public void placeSignUpView(JPanel panel) { // 패널 구성
        panel.setLayout(null);
        JLabel lblName = new JLabel("*이름");
        lblName.setBounds(10, 10, 80, 25);
        panel.add(lblName);

        JTextField txtName = new JTextField(20);
        txtName.setBounds(80, 10, 180, 25);
        panel.add(txtName);

        JLabel lblPwd = new JLabel("*비밀번호");
        lblPwd.setBounds(10, 50, 80, 25);
        panel.add(lblPwd);

        JPasswordField txtPwd = new JPasswordField(20);
        txtPwd.setBounds(80, 50, 180, 25);
        panel.add(txtPwd);

        JLabel lblYear = new JLabel("*출생 연도");
        lblYear.setBounds(10, 90, 80, 25);
        panel.add(lblYear);

        JTextField txtYear = new JTextField();
        txtYear.setBounds(80, 90, 180, 25);
        panel.add(txtYear);

        JLabel lblSex = new JLabel("*성별");
        lblSex.setBounds(10, 130, 80, 25);
        panel.add(lblSex);

        ButtonGroup bgSex = new ButtonGroup();
        radMale = new JRadioButton("남자");
        radMale.setBounds(75, 130, 60, 25);
        bgSex.add(radMale);
        panel.add(radMale);
        radMale.addActionListener(this);

        radFemale = new JRadioButton("여자");
        radFemale.setBounds(140, 130, 60, 25);
        bgSex.add(radFemale);
        panel.add(radFemale);
        radFemale.addActionListener(this);

        JButton btnSignUp = new JButton("회원가입");
        btnSignUp.setBounds(10, 165, 245, 25);
        panel.add(btnSignUp);
        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText();
                String  pwd = String.valueOf(txtPwd.getPassword());
                int year = Integer.parseInt(txtYear.getText());

                UserInfoManager uim = new UserInfoManager();
                if (uim.setInfo(name, pwd, year, sex)) {    // pwd 가 중복이 아닐경우 DB에 회원정보 저장
                    JOptionPane.showMessageDialog(null, "회원가입 성공");
                    dispose();
                }
                else    // pwd 가 중복이면 저장하지 않음
                    JOptionPane.showMessageDialog(null, "이미 사용 중인 비밀번호입니다");
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
