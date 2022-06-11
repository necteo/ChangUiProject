package FoodNutrientManagement;

import java.io.Serializable;

public class LimitNutrient implements Serializable {
    private int sex;
    private int age;
    private int calorieLimit;
    private int carbLimit;
    private int proteinLimit;

    public LimitNutrient(int sex, int age, int calorieLimit, int carbLimit, int proteinLimit) {
        this.sex = sex;
        this.age = age;
        this.calorieLimit = calorieLimit;
        this.carbLimit = carbLimit;
        this.proteinLimit = proteinLimit;
    }

    public int getAge() { return age; }
    public int getCalorieLimit() { return calorieLimit; }
    public int getCarbLimit() { return carbLimit; }
    public int getProteinLimit() { return proteinLimit; }
    public int getSex() { return sex; }

    public void setAge(int age) { this.age = age; }
    public void setCalorieLimit(int calorieLimit) { this.calorieLimit = calorieLimit; }
    public void setCarbLimit(int carbLimit) { this.carbLimit = carbLimit; }
    public void setProteinLimit(int proteinLimit) { this.proteinLimit = proteinLimit; }
    public void setSex(int sex) { this.sex = sex; }
}
