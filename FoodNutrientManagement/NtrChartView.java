package FoodNutrientManagement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NtrChartView extends JFrame {  // 차트 출력 화면 클래스

    private final String[] times = new String[]{"아침", "점심", "저녁"};
    private int cur = 0;            // 차트에 출력중인 날짜 저장
    private final int limit = 6;    // 한 차트에 출력 최대로 가능한 수

    public NtrChartView(ArrayList<DailyNutrient> dnList, boolean[] isNtrsChecked, int chartMode, LimitNutrient limitNtr) {  // 생성자에서 기본 화면 생성
        ArrayList<DailyNutrient> noTimednList = new ArrayList<>();

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
                if (chartMode == 0) {
                    if (cur >= limit) {
                        cur -= limit;
                        placeChartPanel(panel, noTimednList, isNtrsChecked, chartMode, limitNtr);
                        panel.remove(2);
                        panel.revalidate();
                        panel.repaint();
                    }
                } else if (chartMode == 1) {
                    if (cur >= limit) {
                        cur -= limit;
                        placeChartPanel(panel, dnList, isNtrsChecked, chartMode, limitNtr);
                        panel.remove(2);
                        panel.revalidate();
                        panel.repaint();
                    }
                }
            }
        });

        JButton btnNext = new JButton("다음");
        btnNext.setBounds(820, 364, 60, 20);
        panel.add(btnNext);
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // 다음 기간 차트 출력
                if (chartMode == 0) {
                    if (cur < noTimednList.size() - limit) {
                        cur += limit;
                        placeChartPanel(panel, noTimednList, isNtrsChecked, chartMode, limitNtr);
                        panel.remove(2);
                        panel.revalidate();
                        panel.repaint();
                    }
                } else if (chartMode == 1) {
                    if (cur < dnList.size() - limit) {
                        cur += limit;
                        placeChartPanel(panel, dnList, isNtrsChecked, chartMode, limitNtr);
                        panel.remove(2);
                        panel.revalidate();
                        panel.repaint();
                    }
                }
            }
        });

        if (chartMode == 0) {
            for (DailyNutrient dn : dnList) {
                boolean isDate = false;
                for (DailyNutrient noTimedn : noTimednList) {
                    if (noTimedn.getDate().equals(dn.getDate())) {
                        isDate = true;
                        noTimedn.setCalories(noTimedn.getCalories() + dn.getCalories());
                        noTimedn.setCarbohydrate(noTimedn.getCarbohydrate() + dn.getCarbohydrate());
                        noTimedn.setProtein(noTimedn.getProtein() + dn.getProtein());
                        noTimedn.setFat(noTimedn.getFat() + dn.getFat());
                    }
                }
                if (!isDate) {
                    noTimednList.add(dn);
                }
            }
        }

        placeChartPanel(panel, chartMode == 0 ? noTimednList : dnList, isNtrsChecked, chartMode, limitNtr);     // 차트 출력

        setVisible(true);
    }

    public void placeChartPanel(JPanel panel, ArrayList<DailyNutrient> dnList, boolean[] isNtrsChecked, int chartMode, LimitNutrient limitNtr) {    // 차트 패널
        JFreeChart chart = ChartFactory.createBarChart("일일 섭취 영양소",
                "날짜", "섭취량", createDataset(dnList, isNtrsChecked, chartMode));
        chart.getTitle().setFont(new Font("나눔바른고딕", Font.BOLD, 15));
        CategoryPlot plot = chart.getCategoryPlot();
        ValueAxis range = plot.getRangeAxis();
        ValueMarker calMarker = null;
        ValueMarker carbMarker = null;
        ValueMarker proMarker = null;
        if (chartMode == 0) {
            calMarker = new ValueMarker(limitNtr.getCalorieLimit());
            carbMarker = new ValueMarker(limitNtr.getCarbLimit());
            proMarker = new ValueMarker(limitNtr.getProteinLimit());
        } else if (chartMode == 1) {
            calMarker = new ValueMarker(limitNtr.getCalorieLimit() / 3);
            carbMarker = new ValueMarker(limitNtr.getCarbLimit() / 3);
            proMarker = new ValueMarker(limitNtr.getProteinLimit() / 3);
        }
        calMarker.setLabelFont(new Font("굴림", Font.BOLD, 13));
        calMarker.setLabel("일일권장칼로리");
        calMarker.setLabelTextAnchor(TextAnchor.BASELINE_LEFT);
        calMarker.setPaint(Color.RED);

        carbMarker.setLabelFont(new Font("굴림", Font.BOLD, 13));
        carbMarker.setLabel("일일권장탄수화물");
        carbMarker.setLabelTextAnchor(TextAnchor.BASELINE_LEFT);
        carbMarker.setPaint(Color.BLUE);

        proMarker.setLabelFont(new Font("굴림", Font.BOLD, 13));
        proMarker.setLabel("일일권장단백질");
        proMarker.setLabelTextAnchor(TextAnchor.BASELINE_LEFT);
        proMarker.setPaint(Color.GREEN);

        plot.addRangeMarker(calMarker);
        plot.addRangeMarker(carbMarker);
        plot.addRangeMarker(proMarker);
        plot.getDomainAxis().setLabelFont(new Font("바탕", Font.BOLD, 13));
        plot.getDomainAxis().setTickLabelFont(new Font("굴림", Font.PLAIN, 11));
        plot.getRangeAxis().setLabelFont(new Font("굴림", Font.BOLD, 13));
        plot.getRangeAxis().setTickLabelFont(new Font("굴림", Font.PLAIN, 11));
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(0, 0, 884, 360);
        panel.add(chartPanel);
    }

    public DefaultCategoryDataset createDataset(ArrayList<DailyNutrient> dnList, boolean[] isNtrsChecked, int chartMode) {  // 차트에 출력할 데이터셋 생성
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series_calories = "Calories";
        String series_carbohydrate = "Carbohydrate";
        String series_protein = "Protein";
        String series_fat = "Fat";

        if (chartMode == 0) {
            ArrayList<DailyNutrient> noTimednList = new ArrayList<>();
            for (DailyNutrient dn: dnList) {
                boolean isDate = false;
                for (DailyNutrient noTimedn: noTimednList) {
                    if (noTimedn.getDate().equals(dn.getDate())) {
                        isDate = true;
                        noTimedn.setCalories(noTimedn.getCalories() + dn.getCalories());
                        noTimedn.setCarbohydrate(noTimedn.getCarbohydrate() + dn.getCarbohydrate());
                        noTimedn.setProtein(noTimedn.getProtein() + dn.getProtein());
                        noTimedn.setFat(noTimedn.getFat() + dn.getFat());
                    }
                }
                if (!isDate) {
                    noTimednList.add(dn);
                }
            }
            for (int i = cur; i < Math.min(cur + limit, noTimednList.size()); i++) {
                DailyNutrient dn = noTimednList.get(i);
                String date = dn.getDate();
                if (isNtrsChecked[0])   // isCaloriesChecked
                    dataset.addValue(dn.getCalories(), series_calories, date);
                if (isNtrsChecked[1])   // isCarbohydrateChecked
                    dataset.addValue(dn.getCarbohydrate(), series_carbohydrate, date);
                if (isNtrsChecked[2])   // isProteinChecked
                    dataset.addValue(dn.getProtein(), series_protein, date);
                if (isNtrsChecked[3])   // isFatChecked
                    dataset.addValue(dn.getFat(), series_fat, date);
            }
        } else if (chartMode == 1) {
            for (int i = cur; i < Math.min(cur + limit, dnList.size()); i++) {
                DailyNutrient dn = dnList.get(i);
                String date = dn.getDate();
                int time = dn.getTime();
                if (isNtrsChecked[0])   // isCaloriesChecked
                    dataset.addValue(dn.getCalories(), series_calories, date + ": " + times[time]);
                if (isNtrsChecked[1])   // isCarbohydrateChecked
                    dataset.addValue(dn.getCarbohydrate(), series_carbohydrate, date + ": " + times[time]);
                if (isNtrsChecked[2])   // isProteinChecked
                    dataset.addValue(dn.getProtein(), series_protein, date + ": " + times[time]);
                if (isNtrsChecked[3])   // isFatChecked
                    dataset.addValue(dn.getFat(), series_fat, date + ": " + times[time]);
            }
        }

        return dataset;
    }
}
