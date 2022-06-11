package FoodNutrientManagement;

import java.io.Serial;
import java.io.Serializable;

public class DailyNutrient implements Serializable {        // DB 일일_영양소 테이블 데이터를 저장하는 클래스
    @Serial
    private static final long serialVersionUID = 2L;
    private String date;            // 날짜
    private int time;               // 시간대
    private double calories;        // 열량
    private double carbohydrate;    // 탄수화물
    private double protein;         // 단백질
    private double fat;             // 지방
    private String id;

    public DailyNutrient(String d, int t, double cal, double ch, double p, double f, String id) {
        this.date = d;
        this.time = t;
        this.calories = cal;
        this.carbohydrate = ch;
        this.protein = p;
        this.fat = f;
        this.id = id;
    }

    public String getDate() { return this.date; }
    public int getTime() { return this.time; }
    public double getCalories() { return this.calories; }
    public double getCarbohydrate() { return this.carbohydrate; }
    public double getProtein() { return this.protein; }
    public double getFat() { return this.fat; }
    public String getId() { return id; }

    public void setDate(String date) { this.date = date; }
    public void setTime(int time) { this.time = time; }
    public void setCalories(double calories) { this.calories = calories; }
    public void setCarbohydrate(double carbohydrate) { this.carbohydrate = carbohydrate; }
    public void setProtein(double protein) { this.protein = protein; }
    public void setFat(double fat) { this.fat = fat; }
    public void setId(String id) { this.id = id; }
}
