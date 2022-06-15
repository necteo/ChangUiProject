package FoodNutrientManagement;

import SystemManagement.Client;
import SystemManagement.Protocol;
import UserManagement.UserInfoManager;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FoodNtrView extends JFrame implements ActionListener {     // 식품명을 저장하는 화면으로 가장 메인이 되는 화면 클래스
    private JRadioButton radBreakfast;
    private JRadioButton radLunch;
    private JRadioButton radDinner;
    private int time = 0;
    private Client client;
    private ArrayList<FoodNutrient> foodNtrList;
    private DefaultListModel model;
    private JRadioButton radDaily;
    private JRadioButton radTime;
    private int chartMode = 0;

    public static void main(String[] args) {   // 로그인 건너뛰고 테스트용
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FoodNtrView(new Client(),"hong");
            }
        });
    }
    public FoodNtrView(Client c, String id) {      // 생성자에서 기본 화면 생성돼
        setTitle("식품 영양소 관리");
        setBounds(500, 300, 450, 490);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        placeFoodNtrPanel(panel, id);

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setBounds(15, 130, 400, 1);
        panel.add(separator);

        placeFoodRcmPanel(panel, id);

        JSeparator separator1 = new JSeparator(JSeparator.HORIZONTAL);
        separator1.setBounds(15, 260, 400, 1);
        panel.add(separator1);

        placeChartPanel(panel, id);

        add(panel);
        setVisible(true);

        client = c;
        foodNtrList = new ArrayList<>();

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

    public void placeFoodNtrPanel(JPanel panel, String id) {   // 식품명을 입력받는 패널
        panel.setBounds(0, 0, 450, 490);
        getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblFood = new JLabel("식품명");
        lblFood.setBounds(15, 30, 40, 20);
        panel.add(lblFood);

        AutoSuggest txtAutoSuggest = new AutoSuggest();
        txtAutoSuggest.setBounds(75, 30, 180, 20);
        panel.add(txtAutoSuggest);

        JLabel lblTime = new JLabel("시간대");
        lblTime.setBounds(15, 80, 40, 20);
        panel.add(lblTime);

        ButtonGroup bgTime = new ButtonGroup();
        radBreakfast = new JRadioButton("아침");
        radBreakfast.setBounds(75, 80, 50, 20);
        bgTime.add(radBreakfast);
        panel.add(radBreakfast);
        radBreakfast.setSelected(true);
        radBreakfast.addActionListener(this);

        radLunch = new JRadioButton("점심");
        radLunch.setBounds(140, 80, 50, 20);
        bgTime.add(radLunch);
        panel.add(radLunch);
        radLunch.addActionListener(this);

        radDinner = new JRadioButton("저녁");
        radDinner.setBounds(205, 80, 50, 20);
        bgTime.add(radDinner);
        panel.add(radDinner);
        radDinner.addActionListener(this);

        JButton btnInput = new JButton("식품 정보 저장");
        btnInput.setBounds(290, 35, 120, 60);
        btnInput.addActionListener(this);
        panel.add(btnInput);
        btnInput.addActionListener(new ActionListener() {   // 버튼이 눌리면
            @Override
            public void actionPerformed(ActionEvent e) {

                Thread cw = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            client.protocol = new Protocol(Protocol.PT_REQ_FOOD_CD);
                            System.out.println("식품코드 요청");
                            client.protocol.setFoodName(txtAutoSuggest.getText());
                            client.os.write(client.protocol.getPacket());

                            client.protocol = new Protocol();
                            client.buf = client.protocol.getPacket();
                            client.is.read(client.buf);
                            client.protocol.setPacket(client.buf[0], client.buf);
                            String food_cd = client.protocol.getFoodCd();
                            FoodNutrient foodNtrInfo = GetOpenData.getDataByCode(food_cd);  // 입력된 식품명으로 공공데이터 가져옴

                            client.protocol = new Protocol(Protocol.PT_RES_DAILY_NUTR);
                            client.protocol.setId(id);
                            System.out.println("영양소 정보 전송");
                            client.os.write(client.protocol.getPacket());

                            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            double calories = foodNtrInfo.getCalories();   // 리스트의 첫번째 값으로 저장
                            double carbohydrate = foodNtrInfo.getCarbohydrate();
                            double protein =  foodNtrInfo.getProtein();
                            double fat = foodNtrInfo.getFat();
                            DailyNutrient dn = new DailyNutrient(date, time, calories, carbohydrate, protein, fat, id);     // DB 일일_영양소 테이블 저장용 클래스

                            client.is.read();
                            ObjectOutputStream oos = new ObjectOutputStream(client.os);
                            oos.writeObject(dn);
                            oos.flush();

                            client.protocol = new Protocol();
                            client.buf = client.protocol.getPacket();
                            client.is.read(client.buf);
                            int packetType = client.buf[0];
                            client.protocol.setPacket(packetType, client.buf);
                            if (client.protocol.getDailyNutrResult().equals("0"))
                                JOptionPane.showMessageDialog(null, "영양소 정보 저장 완료");
                            else if (client.protocol.getDailyNutrResult().equals("1"))
                                JOptionPane.showMessageDialog(null, "저장 실패");
                            else
                                JOptionPane.showMessageDialog(null, "일일 권장 영양소를 초과");
                        } catch (IOException | ParseException ex) {
                            throw new RuntimeException(ex);
                        } catch (NullPointerException ex) {
                            JOptionPane.showMessageDialog(null, "식품 정보가 없습니다.");
                        }
                    }
                });

                cw.start();
            }
        });
    }

    public void placeFoodRcmPanel(JPanel panel, String id) {   // 식품 추천 받는 버튼이 있는 패널
        JLabel lblFoods = new JLabel("추천 목록");
        lblFoods.setBounds(15, 185, 80, 20);
        panel.add(lblFoods);

        model = new DefaultListModel();
        JList foodList = new JList(model);
        foodList.setBounds(75, 160, 180, 70);
        JScrollPane scrollPane = new JScrollPane(foodList);
        scrollPane.setBounds(75, 160, 180, 70);
        panel.add(scrollPane);
        foodList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    String food = String.valueOf(foodList.getSelectedValue());
                    Thread cw = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                client.protocol = new Protocol(Protocol.PT_REQ_FOOD_CD);
                                System.out.println("식품코드 요청");
                                client.protocol.setFoodName(food);
                                client.os.write(client.protocol.getPacket());

                                client.protocol = new Protocol();
                                client.buf = client.protocol.getPacket();
                                client.is.read(client.buf);
                                client.protocol.setPacket(client.buf[0], client.buf);
                                String food_cd = client.protocol.getFoodCd();
                                FoodNutrient foodNtrInfo = GetOpenData.getDataByCode(food_cd);  // 입력된 식품명으로 공공데이터 가져옴

                                client.protocol = new Protocol(Protocol.PT_RES_DAILY_NUTR);
                                client.protocol.setId(id);
                                System.out.println("영양소 정보 전송");
                                client.os.write(client.protocol.getPacket());

                                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                double calories = foodNtrInfo.getCalories();   // 리스트의 첫번째 값으로 저장
                                double carbohydrate = foodNtrInfo.getCarbohydrate();
                                double protein =  foodNtrInfo.getProtein();
                                double fat = foodNtrInfo.getFat();
                                DailyNutrient dn = new DailyNutrient(date, time, calories, carbohydrate, protein, fat, id);     // DB 일일_영양소 테이블 저장용 클래스

                                client.is.read();
                                ObjectOutputStream oos = new ObjectOutputStream(client.os);
                                oos.writeObject(dn);
                                oos.flush();

                                client.protocol = new Protocol();
                                client.buf = client.protocol.getPacket();
                                client.is.read(client.buf);
                                int packetType = client.buf[0];
                                client.protocol.setPacket(packetType, client.buf);
                                if (client.protocol.getDailyNutrResult().equals("0"))
                                    JOptionPane.showMessageDialog(null, "영양소 정보 저장 완료");
                                else if (client.protocol.getDailyNutrResult().equals("1"))
                                    JOptionPane.showMessageDialog(null, "저장 실패");
                                else
                                    JOptionPane.showMessageDialog(null, "일일 권장 영양소를 초과");
                            } catch (IOException | ParseException ex) {
                                throw new RuntimeException(ex);
                            } catch (NullPointerException ex) {
                                JOptionPane.showMessageDialog(null, "식품 정보가 없습니다.");
                                try {
                                    client.is.read();
                                    ObjectOutputStream oos = new ObjectOutputStream(client.os);
                                    oos.writeObject(null);
                                    oos.flush();
                                    client.is.read();   // 쓰레기 값 처리
                                } catch (IOException exc) {
                                    throw new RuntimeException(exc);
                                }
                            }
                        }
                    });

                    cw.start();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        Vector<Integer> rcmNum = new Vector<Integer>();
        for (int i = 1; i <= 10; i++) {
            rcmNum.add(i);
        }
        JComboBox cbxRcmNum = new JComboBox(new DefaultComboBoxModel(rcmNum));
        cbxRcmNum.setBounds(290, 210, 120, 20);
        panel.add(cbxRcmNum);

        JButton btnFoodRecommend = new JButton("식품 추천");
        btnFoodRecommend.setBounds(290, 160, 120, 40);
        panel.add(btnFoodRecommend);
        btnFoodRecommend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 식품 추천 화면 출력
                Thread cw = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            client.protocol = new Protocol(Protocol.PT_RECOMMEND_FOOD);
                            client.protocol.setId(id);
                            client.protocol.setRcmNum(String.valueOf(cbxRcmNum.getSelectedItem()));
                            client.os.write(client.protocol.getPacket());
                            System.out.println("식품 추천 요청");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

                Thread cr = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (foodNtrList) {
                            try {
                                ObjectInputStream ois = new ObjectInputStream(client.is);
                                foodNtrList = (ArrayList<FoodNutrient>) ois.readObject();
                                System.out.println("식품 정보를 받음");
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (ClassNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                });

//                while (true) {
//                    synchronized (foodNtr) {
//                        if (foodNtr.getCalories() < 800)
//                            break;
                        cw.start();
                        cr.start();
//                    }
//                }

                try {
                    cr.join();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                for (FoodNutrient foodNtr: foodNtrList) {
                    model.addElement(foodNtr.getName());
                }
            }
        });
    }

    public void placeChartPanel(JPanel panel, String id) {     // 차트 출력을 위한 기간을 입력받는 패널
        JLabel lblTerm = new JLabel("기간");
        lblTerm.setBounds(15, 295, 40, 20);
        panel.add(lblTerm);

        final Date firstYear = Date.valueOf(LocalDate.parse("2022-01-01"));
        final Date lastYear = Date.valueOf(LocalDate.now());
        SpinnerDateModel year = new SpinnerDateModel(lastYear, firstYear, lastYear, Calendar.YEAR);
        SpinnerNumberModel month = new SpinnerNumberModel(1, 1, 12, 1);
        SpinnerNumberModel day = new SpinnerNumberModel(1, 1, 31, 1);

        JSpinner spnStartYear = new JSpinner(year);
        spnStartYear.setBounds(70, 295, 50, 20);
        spnStartYear.setEditor(new JSpinner.DateEditor(spnStartYear, "yyyy"));
        panel.add(spnStartYear);

        JLabel lblStartYear = new JLabel("년");
        lblStartYear.setBounds(125, 295, 25, 20);
        panel.add(lblStartYear);

        JSpinner spnStartMonth = new JSpinner(month);
        spnStartMonth.setBounds(150, 295, 35, 20);
        panel.add(spnStartMonth);

        JLabel lblStartMonth = new JLabel("월");
        lblStartMonth.setBounds(190, 295, 25, 20);
        panel.add(lblStartMonth);

        JSpinner spnStartDay = new JSpinner(day);
        spnStartDay.setBounds(215, 295, 35, 20);
        panel.add(spnStartDay);
        spnStartDay.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {   // 일수를 1에서 28~31로 제한하는 기능
                int[] dayOfMonth = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
                int year = Integer.parseInt(spnStartYear.getValue().toString().substring(24));
                int month = (int) spnStartMonth.getValue();
                int day = (int) spnStartDay.getValue();
                int maxDay = dayOfMonth[month - 1];

                if (month == 4 && year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                    maxDay -= 1;
                if (day < 1)
                    spnStartDay.setValue(1);
                if (day > maxDay)
                    spnStartDay.setValue(maxDay);
            }
        });

        JLabel lblStartDay = new JLabel("일");
        lblStartDay.setBounds(255, 295, 25, 20);
        panel.add(lblStartDay);

        JLabel lblTo = new JLabel("~");
        lblTo.setBounds(24, 335, 40, 20);
        panel.add(lblTo);

        SpinnerDateModel _year = new SpinnerDateModel(lastYear, firstYear, lastYear, Calendar.YEAR);
        SpinnerNumberModel _month = new SpinnerNumberModel(7, 1, 12, 1);
        SpinnerNumberModel _day = new SpinnerNumberModel(1, 1, 31, 1);

        JSpinner spnEndYear = new JSpinner(_year);
        spnEndYear.setBounds(70, 335, 50, 20);
        spnEndYear.setEditor(new JSpinner.DateEditor(spnEndYear, "yyyy"));
        panel.add(spnEndYear);

        JLabel lblEndYear = new JLabel("년");
        lblEndYear.setBounds(125, 335, 25, 20);
        panel.add(lblEndYear);

        JSpinner spnEndMonth = new JSpinner(_month);
        spnEndMonth.setBounds(150, 335, 35, 20);
        panel.add(spnEndMonth);

        JLabel lblEndMonth = new JLabel("월");
        lblEndMonth.setBounds(190, 335, 25, 20);
        panel.add(lblEndMonth);

        JSpinner spnEndDay = new JSpinner(_day);
        spnEndDay.setBounds(215, 335, 35, 20);
        panel.add(spnEndDay);
        spnEndDay.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {   // 위와 똑같다
                int[] dayOfMonth = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
                int year = Integer.parseInt(spnEndYear.getValue().toString().substring(24));
                int month = (int) spnEndMonth.getValue();
                int day = (int) spnEndDay.getValue();
                int maxDay = dayOfMonth[month - 1];

                if (month == 4 && year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                    maxDay -= 1;
                if (day < 1)
                    spnEndDay.setValue(1);
                if (day > maxDay)
                    spnEndDay.setValue(maxDay);
            }
        });

        JLabel lblEndDay = new JLabel("일");
        lblEndDay.setBounds(255, 335, 25, 20);
        panel.add(lblEndDay);

        JButton btnResult = new JButton("통계 차트 보기");
        btnResult.setBounds(290, 295, 120, 60);
        btnResult.addActionListener(this);
        panel.add(btnResult);

        JLabel lblNtrs = new JLabel("통계 표시 영양소");
        lblNtrs.setBounds(18, 375, 100, 20);
        panel.add(lblNtrs);

        JCheckBox chbCal = new JCheckBox("칼로리", true);
        chbCal.setBounds(130, 375, 70, 20);
        panel.add(chbCal);

        JCheckBox chbCarb = new JCheckBox("탄수화물", true);
        chbCarb.setBounds(200, 375, 80, 20);
        panel.add(chbCarb);

        JCheckBox chbPro = new JCheckBox("단백질", true);
        chbPro.setBounds(285, 375, 70, 20);
        panel.add(chbPro);

        JCheckBox chbFat = new JCheckBox("지방", true);
        chbFat.setBounds(360, 375, 60, 20);
        panel.add(chbFat);

        JLabel lblChartMode = new JLabel("통계 차트 유형");
        lblChartMode.setBounds(18, 415, 100, 20);
        panel.add(lblChartMode);

        ButtonGroup bgChartMode = new ButtonGroup();
        radDaily = new JRadioButton("날짜별");
        radDaily.setBounds(130, 415, 70, 20);
        bgChartMode.add(radDaily);
        panel.add(radDaily);
        radDaily.setSelected(true);
        radDaily.addActionListener(this);

        radTime = new JRadioButton("시간대별");
        radTime.setBounds(245, 415, 90, 20);
        bgChartMode.add(radTime);
        panel.add(radTime);
        radTime.addActionListener(this);

        btnResult.addActionListener(new ActionListener() {  // 통계 차트 표시 버튼
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(chbCal.isSelected() || chbCarb.isSelected() || chbPro.isSelected() || chbFat.isSelected())) {
                    JOptionPane.showMessageDialog(null, "하나 이상의 항목을 체크해야합니다.");
                    return;
                }
                Thread cw = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            client.protocol = new Protocol(Protocol.PT_RES_CHART_DATE);
                            System.out.println("통계 차트 날짜 정보 전송");

                            String year = spnStartYear.getValue().toString().substring(24);
                            String month = String.valueOf(spnStartMonth.getValue());
                            String day = String.valueOf(spnStartDay.getValue());
                            String _year = spnEndYear.getValue().toString().substring(24);
                            String _month = String.valueOf(spnEndMonth.getValue());
                            String _day = String.valueOf(spnEndDay.getValue());          // 조건으로 준 기간에 따라 DB 에서 읽어온 데이터를 List 로 저장
                            client.protocol.setId(id);
                            client.protocol.setDate(new String[]{year, month, day}, new String[]{_year, _month, _day});
                            client.os.write(client.protocol.getPacket());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                Thread cr = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ObjectInputStream ois = new ObjectInputStream(client.is);
                            ArrayList<DailyNutrient> dnList = (ArrayList<DailyNutrient>) ois.readObject();
                            System.out.println("영양소 정보를 받음");
                            client.os.write('o');
                            LimitNutrient limitNtr = (LimitNutrient) ois.readObject();
                            System.out.println("영양소 권장치 정보를 받음");

                            boolean[] isNtrsChecked = new boolean[]{
                                    chbCal.isSelected(),
                                    chbCarb.isSelected(),
                                    chbPro.isSelected(),
                                    chbFat.isSelected()};
                            new NtrChartView(dnList, isNtrsChecked, chartMode, limitNtr);    // 차트 화면 출력
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

                cw.start();
                cr.start();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {    // 아침, 점심, 저녁 선택에 따라 바꿈
        String s = e.getActionCommand();
        if (s.equals(radBreakfast.getText()))
            time = 0;
        else if (s.equals(radLunch.getText()))
            time = 1;
        else if (s.equals(radDinner.getText()))
            time = 2;
        if (s.equals(radDaily.getText()))
            chartMode = 0;
        else if (s.equals(radTime.getText()))
            chartMode = 1;
    }
}
