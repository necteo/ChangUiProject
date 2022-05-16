package FoodNutrientManagement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NtrChartView extends JFrame {  // 차트 출력 화면 클래스

    private final String[] times = new String[]{"아침", "점심", "저녁"};
    private int cur = 0;            // 차트에 출력중인 날짜 저장
    private final int limit = 6;    // 한 차트에 출력 최대로 가능한 수

    public NtrChartView(ArrayList<DailyNutrient> dnList) {  // 생성자에서 기본 화면 생성
        setTitle("일일 섭취 영양소 통계");
        setBounds(300, 300, 900, 425);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 900, 420);
        getContentPane().add(panel);
        panel.setLayout(null);

        JButton btnPrev = new JButton("이전");
        btnPrev.setBounds(750, 364, 60, 20);
        panel.add(btnPrev);
        btnPrev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // 이전 기간 차트 출력
                if (cur >= limit) {
                    cur -= limit;
                    placeChartPanel(panel, dnList);
                    panel.remove(2);
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });

        JButton btnNext = new JButton("다음");
        btnNext.setBounds(820, 364, 60, 20);
        panel.add(btnNext);
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // 다음 기간 차트 출력
                if (cur <= dnList.size() - limit) {
                    cur += limit;
                    placeChartPanel(panel, dnList);
                    panel.remove(2);
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });

        placeChartPanel(panel, dnList);     // 차트 출력

        setVisible(true);
    }

    public void placeChartPanel(JPanel panel, ArrayList<DailyNutrient> dnList) {    // 차트 패널
        JFreeChart chart = ChartFactory.createBarChart("일일 섭취 영양소",
                "날짜", "섭취량", createDataset(dnList));
        chart.getTitle().setFont(new Font("나눔바른고딕", Font.BOLD, 15));
        CategoryPlot plot = chart.getCategoryPlot();
        plot.getDomainAxis().setLabelFont(new Font("바탕", Font.BOLD, 13));
        plot.getDomainAxis().setTickLabelFont(new Font("굴림", Font.PLAIN, 11));
        plot.getRangeAxis().setLabelFont(new Font("굴림", Font.BOLD, 13));
        plot.getRangeAxis().setTickLabelFont(new Font("굴림", Font.PLAIN, 11));
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(0, 0, 884, 360);
        panel.add(chartPanel);
    }

    public DefaultCategoryDataset createDataset(ArrayList<DailyNutrient> dnList) {  // 차트에 출력할 데이터셋 생성
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series_calories = "Calories";
        String series_carbohydrate = "Carbohydrate";
        String series_protein = "Protein";
        String series_fat = "Fat";

        for (int i = cur; i < Math.min(cur + limit, dnList.size()); i++) {
            DailyNutrient dn = dnList.get(i);
            String date = dn.getDate();
            int time = dn.getTime();
            dataset.addValue(dn.getCalories(), series_calories, date + ": " + times[time]);
            dataset.addValue(dn.getCarbohydrate(), series_carbohydrate, date + ": " + times[time]);
            dataset.addValue(dn.getProtein(), series_protein, date + ": " + times[time]);
            dataset.addValue(dn.getFat(), series_fat, date + ": " + times[time]);
        }

        return dataset;
    }
}
