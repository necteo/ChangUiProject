package FoodNutrientManagement;

public class FoodNutrient {         // 공공데이터 API 에서 받아온 데이터를 저장하는 클래스
    private String name;            // 식품명
    private double calories;        // 열량
    private double carbohydrate;    // 탄수화물
    private double protein;         // 단백질
    private double fat;             // 지방

    public void setName(String name) { this.name = name; }
    public void setCalories(double calories) { this.calories = calories; }
    public void setCarbohydrate(double carbohydrate) { this.carbohydrate = carbohydrate; }
    public void setProtein(double protein) { this.protein = protein; }
    public void setFat(double fat) { this.fat = fat; }

    public String getName() { return this.name; }
    public double getCalories() { return this.calories; }
    public double getCarbohydrate() { return this.carbohydrate; }
    public double getProtein() { return this.protein; }
    public double getFat() { return this.fat; }
}
