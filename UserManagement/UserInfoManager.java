package UserManagement;

import SystemManagement.DBController;

public class UserInfoManager {
    DBController db = new DBController();

    public boolean setInfo(String name, String pwd, int year, int sex) {
        if (isDuplicate(pwd))
            return false;
        try {
            db.dbConn();
            String sql = "insert into user_info values (?, ?, ?, ?)";
            db.pst = db.conn.prepareStatement(sql);
            db.pst.setString(1, name);
            db.pst.setString(2, pwd);
            db.pst.setInt(3, year);
            db.pst.setInt(4, sex);
            db.pst.executeUpdate();
            db.dbClose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public String getInfo(String n, String p) { // name, password
        String name = "";
        String pwd = "";
        try {
            db.dbConn();
            String sql = "select name, password from user_info where name=? and password=?";
            db.pst = db.conn.prepareStatement(sql);
            db.pst.setString(1, n);
            db.pst.setString(2, p);
            db.rs = db.pst.executeQuery();
            while (db.rs.next()) {
                name = db.rs.getString("name");
                pwd = db.rs.getString("password");
            }
            db.rs.close();
            db.dbClose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name + "," + pwd;
    }

    public boolean isDuplicate(String pwd) {
        boolean isDup = false;
        try {
            db.dbConn();
            String sql = "select password from user_info where password=?";
            db.pst = db.conn.prepareStatement(sql);
            db.pst.setString(1, pwd);
            db.rs = db.pst.executeQuery();
            if (db.rs.next())
                isDup = true;
            db.rs.close();
            db.dbClose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDup;
    }
}
