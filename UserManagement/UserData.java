package UserManagement;

import javax.swing.*;
import java.time.LocalDate;

public class UserData {
    private String userName;
    private int sex;
    private int age;
    private int dailyCalorie = 0;
    private int dailyProtein = 0;
    private int calorieLimit = 0;
    private int carbohydrate = 130;
    private int proteinLimit = 0;

    public  UserData (String n, int y, int s)
    {
        sex =s;
        LocalDate now = LocalDate.now();
        int curY= now.getYear();
        age = curY-y;
        userName = n;
    }

    public int GetCalorie()
    {
        return this.calorieLimit;
    }

    public int GetProtein()
    {
        return this.proteinLimit;
    }

    public void addCalorie(int c)
    {
        if(this.calorieLimit > this.dailyCalorie + c)
        {
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "경고: 하루 권장 칼로리량을 초과합니다!");
        }
        else
        {
           this.dailyCalorie +=c;
        }
    }

    public void addProtein(int p)
    {
        if(this.proteinLimit > this.dailyCalorie + p)
        {
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "경고: 하루 권장 단백질량을 초과합니다!");
        }
        else
        {
            this.dailyProtein +=p;
        }
    }

    public void SetDietaryReferenceIntakes(String s, int c, int a)
    {
        if (c == 0)
        {
            if(a >= 75)
            {
                calorieLimit = 1900;
                proteinLimit = 60;
            }
            else if (a >= 65)
            {
                calorieLimit = 2000;
                proteinLimit = 60;

            }
            else if (a >= 50)
            {
                calorieLimit = 2200;
                proteinLimit = 60;
            }
            else if (a >= 30)
            {
                calorieLimit = 2500;
                proteinLimit = 65;
            }
            else if (a >= 19)
            {
                calorieLimit = 2600;
                proteinLimit = 65;
            }
            else if (a >= 15)
            {
                calorieLimit = 2700;
                proteinLimit = 65;
            }
            else if (a >= 12)
            {
                calorieLimit = 2500;
                proteinLimit = 60;
            }
            else if (a >= 9)
            {
                calorieLimit = 2000;
                proteinLimit = 50;
            }
            else if (a >= 6)
            {
                calorieLimit = 1700;
                proteinLimit = 35;
            }
            else
            {
              //유아
            }
        }
        else
        {
            if (a >= 75)
            {

                calorieLimit = 1500;
                proteinLimit = 50;
            }
            else if (a >= 65)
            {
                calorieLimit = 1600;
                proteinLimit = 50;
            }
            else if (a >= 50)
            {
                calorieLimit = 1700;
                proteinLimit = 50;
            }
            else if (a >= 30)
            {
                calorieLimit = 1900;
                proteinLimit = 50;
            }
            else if (a >= 19)
            {
                calorieLimit = 2000;
                proteinLimit = 55;
            }
            else if (a >= 15)
            {
                calorieLimit = 2000;
                proteinLimit = 55;
            }
            else if (a >= 12)
            {
                calorieLimit = 2000;
                proteinLimit = 55;
            }
            else if (a >= 9)
            {
                calorieLimit = 1800;
                proteinLimit = 45;
            }
            else if (a >= 6)
            {
                calorieLimit = 1500;
                proteinLimit = 35;
            }
            else
            {
              //유아
            }
        }

    }

}
