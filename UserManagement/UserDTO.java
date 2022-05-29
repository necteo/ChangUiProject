package UserManagement;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String password;
    private String name;
    private int year;
    private int sex;

    public UserDTO(String id, String password, String name, int year, int sex) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.year = year;
        this.sex = sex;
    }

    public void setId(String id) { this.id = id; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setYear(int year) { this.year = year; }
    public void setSex(int sex) { this.sex = sex; }

    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public int getYear() { return year; }
    public int getSex() { return sex; }
}
