package FoodNutrientManagement;

public class DailyNutrient {
    private String date;
    private int time;
    private double calories;
    private double carbohydrate;
    private double protein;
    private double fat;

    public void setDate(String date) { this.date = date; }
    public void setTime(int time) { this.time = time; }
    public void setCalories(double calories) { this.calories = calories; }
    public void setCarbohydrate(double carbohydrate) { this.carbohydrate = carbohydrate; }
    public void setProtein(double protein) { this.protein = protein; }
    public void setFat(double fat) { this.fat = fat; }

    public String getDate() { return this.date; }
    public int getTime() { return this.time; }
    public double getCalories() { return this.calories; }
    public double getCarbohydrate() { return this.carbohydrate; }
    public double getProtein() { return this.protein; }
    public double getFat() { return this.fat; }
}
