package FoodNutrientManagement;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;

public class FoodNtrView extends JFrame implements ActionListener {     // 식품명을 저장하는 화면으로 가장 메인이 되는 화면 클래스
    private JRadioButton radBreakfast;
    private JRadioButton radLunch;
    private JRadioButton radDinner;
    private int time;

    public static void main(String[] args) {   // 로그인 건너뛰고 테스트용
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FoodNtrView();
            }
        });
    }
    public FoodNtrView() {      // 생성자에서 기본 화면 생성돼
        setTitle("식품 영양소 관리");
        setBounds(500, 300, 450, 430);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        placeFoodNtrPanel(panel);

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setBounds(15, 130, 400, 1);
        panel.add(separator);

        placeFoodRcmPanel(panel);

        JSeparator separator1 = new JSeparator(JSeparator.HORIZONTAL);
        separator1.setBounds(15, 260, 400, 1);
        panel.add(separator1);

        placeChartPanel(panel);

        add(panel);
        setVisible(true);
    }

    public void placeFoodNtrPanel(JPanel panel) {   // 식품명을 입력받는 패널
        panel.setBounds(0, 0, 450, 300);
        getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblFood = new JLabel("식품명");
        lblFood.setBounds(15, 30, 40, 20);
        panel.add(lblFood);

//        JTextField txtFood = new JTextField();
//        txtFood.setBounds(75, 30, 180, 20);
//        panel.add(txtFood);

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
                NtrDataManager ndm = new NtrDataManager();

                try {
                    ArrayList<FoodNutrient> foodNtrInfoList = GetOpenData.getData(txtAutoSuggest.getText());  // 입력된 식품명으로 공공데이터 가져옴
                    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    double calories = foodNtrInfoList.get(0).getCalories();   // 리스트의 첫번째 값으로 저장
                    double carbohydrate = foodNtrInfoList.get(0).getCarbohydrate();
                    double protein =  foodNtrInfoList.get(0).getProtein();
                    double fat = foodNtrInfoList.get(0).getFat();
                    DailyNutrient dn = new DailyNutrient(date, time, calories, carbohydrate, protein, fat);     // DB 일일_영양소 테이블 저장용 클래스
                    ndm.insertData(dn); // DB에 데이터 저장
                } catch (IOException | ParseException ex) {
                    throw new RuntimeException(ex);
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(null, "식품 정보가 없습니다.");
                }
            }
        });
    }

    public void placeFoodRcmPanel(JPanel panel) {   // 식품 추천 받는 버튼이 있는 패널
        JButton btnFoodRecommend = new JButton("식품 추천");
        btnFoodRecommend.setBounds(40, 160, 350, 70);
        panel.add(btnFoodRecommend);
        btnFoodRecommend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 식품 추천 화면 출력
            }
        });
    }

    public void placeChartPanel(JPanel panel) {     // 차트 출력을 위한 기간을 입력받는 패널
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
        SpinnerNumberModel _month = new SpinnerNumberModel(1, 1, 12, 1);
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
        btnResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NtrDataManager ndm = new NtrDataManager();
                int year = Integer.parseInt(spnStartYear.getValue().toString().substring(24));
                int month = (int) spnStartMonth.getValue();
                int day = (int) spnStartDay.getValue();
                int _year = Integer.parseInt(spnEndYear.getValue().toString().substring(24));
                int _month = (int) spnEndMonth.getValue();
                int _day = (int) spnEndDay.getValue();          // 조건으로 준 기간에 따라 DB 에서 읽어온 데이터를 List 로 저장
                ArrayList<DailyNutrient> dnList = ndm.readData(new int[]{year, month, day}, new int[]{_year, _month, _day});
                new NtrChartView(dnList);    // 차트 화면 출력
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
    }
}
