package UserManagement;

import FoodNutrientManagement.FoodNtrView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JPasswordField txtPwd;
    private JTextField txtName;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView();
            }
        });
    }

    public LoginView() {
        setTitle("로그인");
        setSize(280, 150);
        setResizable(false);
        setLocation(700, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        placeLoginPanel(panel);
        add(panel);
        setVisible(true);
    }

    public void placeLoginPanel(JPanel panel){
        panel.setLayout(null);
        JLabel lblName = new JLabel("이름");
        lblName.setBounds(10, 10, 80, 25);
        panel.add(lblName);

        JLabel lblPwd = new JLabel("비밀번호");
        lblPwd.setBounds(10, 40, 80, 25);
        panel.add(lblPwd);

        txtName = new JTextField(20);
        txtName.setBounds(80, 10, 180, 25);
        panel.add(txtName);

        txtPwd = new JPasswordField(20);
        txtPwd.setBounds(80, 40, 180, 25);
        panel.add(txtPwd);
        txtPwd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserInfoManager uim = new UserInfoManager();
                String info = uim.getInfo(txtName.getText(), String.valueOf(txtPwd.getPassword()));
                if (isLoginValid(info)) {
                    dispose();              // 로그인 성공하면 창 닫고 식품정보창을 띄운다
                    new FoodNtrView();
                }
            }
        });

        JButton btnLogin = new JButton("로그인");
        btnLogin.setBounds(135, 77, 120, 25);
        panel.add(btnLogin);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserInfoManager uim = new UserInfoManager();
                String info = uim.getInfo(txtName.getText(), String.valueOf(txtPwd.getPassword()));
                if (isLoginValid(info)) {
                    dispose();              // 로그인 성공하면 창 닫고 식품정보창을 띄운다
                    new FoodNtrView();
                }
            }
        });

        JButton btnSignUp = new JButton("회원가입");
        btnSignUp.setBounds(10, 77, 120, 25);
        panel.add(btnSignUp);
        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUpView();
            }
        });
    }

    public boolean isLoginValid(String info){
        if (info.equals(",")) {   // 해당하는 계정이 없는 경우
            JOptionPane.showMessageDialog(null, "로그인 실패");
            return false;
        }

        String name = info.split(",")[0];
        String pwd = info.split(",")[1];
        if(!txtName.getText().equals(name) || !String.valueOf(txtPwd.getPassword()).equals(pwd)){
            JOptionPane.showMessageDialog(null, "로그인 실패");
            return false;
        }
        JOptionPane.showMessageDialog(null, "로그인 성공");
        return true;
    }
}