package FoodNutrientManagement;

public class FoodNutrient {
    private String name;
    private double calories;
    private double carbohydrate;
    private double protein;
    private double fat;

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
