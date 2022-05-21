package UserManagement;

import javax.swing.*;
import java.time.LocalDate;

public class UserData {
    private String userName="";
    private int sex =0;
    private int age =0;
    private int dailycalorie = 0;
    private int dailyprotein =0;
    private int calorielimit = 0;
    private int carbohydrate = 130;
    private int proteinlimit =0;

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
        return this.calorielimit;
    }

    public int GetProtein()
    {
        return this.proteinlimit;
    }

    public void addCalorie(int c)
    {
        if(this.calorielimit> this.dailycalorie + c)
        {
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "경고: 하루 권장 칼로리량을 초과합니다!");
        }
        else
        {
           this.dailycalorie+=c;
        }
    }

    public void addProtein(int p)
    {
        if(this.proteinlimit> this.dailycalorie + p)
        {
            JFrame jFrame = new JFrame();
            JOptionPane.showMessageDialog(jFrame, "경고: 하루 권장 단백질량을 초과합니다!");
        }
        else
        {
            this.dailyprotein+=p;
        }
    }

    public void SetDietaryReferenceIntakes(String s, int c, int a)
    {
        if (c == 0)
        {
            if(a >= 75)
            {
                calorielimit = 1900;
                proteinlimit = 60;
            }
            else if (a >= 65)
            {
                calorielimit = 2000;
                proteinlimit = 60;

            }
            else if (a >= 50)
            {
                calorielimit = 2200;
                proteinlimit = 60;
            }
            else if (a >= 30)
            {
                calorielimit = 2500;
                proteinlimit = 65;
            }
            else if (a >= 19)
            {
                calorielimit = 2600;
                proteinlimit = 65;
            }
            else if (a >= 15)
            {
                calorielimit = 2700;
                proteinlimit = 65;
            }
            else if (a >= 12)
            {
                calorielimit = 2500;
                proteinlimit = 60;
            }
            else if (a >= 9)
            {
                calorielimit = 2000;
                proteinlimit = 50;
            }
            else if (a >= 6)
            {
                calorielimit = 1700;
                proteinlimit = 35;
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

                calorielimit = 1500;
                proteinlimit = 50;
            }
            else if (a >= 65)
            {
                calorielimit = 1600;
                proteinlimit = 50;
            }
            else if (a >= 50)
            {
                calorielimit = 1700;
                proteinlimit = 50;
            }
            else if (a >= 30)
            {
                calorielimit = 1900;
                proteinlimit = 50;
            }
            else if (a >= 19)
            {
                calorielimit = 2000;
                proteinlimit = 55;
            }
            else if (a >= 15)
            {
                calorielimit = 2000;
                proteinlimit = 55;
            }
            else if (a >= 12)
            {
                calorielimit = 2000;
                proteinlimit = 55;
            }
            else if (a >= 9)
            {
                calorielimit = 1800;
                proteinlimit = 45;
            }
            else if (a >= 6)
            {
                calorielimit = 1500;
                proteinlimit = 35;
            }
            else
            {
              //유아
            }
        }

    }

}
