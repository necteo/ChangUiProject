package UserManagement;

import SystemManagement.DBController;

public class UserInfoManager {  // DB user_info 테이블의 입출력을 담당하는 클래스
    DBController db = new DBController();

    public boolean setInfo(String name, String pwd, int year, int sex) {    // 사용자 정보를 DB 에 저장
        if (isDuplicate(pwd))   // 중복 확인
            return false;       // 중복일 시 false 리턴 후 함수 종료
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

    public String getInfo(String n, String p) { // name, password 데이터를 읽어옴
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
        return name + "," + pwd;    // name, pwd 형태
    }

    public boolean isDuplicate(String pwd) {    // 중복 확인
        boolean isDup = false;
        try {
            db.dbConn();
            String sql = "select password from user_info where password=?";
            db.pst = db.conn.prepareStatement(sql);
            db.pst.setString(1, pwd);
            db.rs = db.pst.executeQuery();
            if (db.rs.next())
                isDup = true;   //  결과값이 있으면 중복
            db.rs.close();
            db.dbClose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDup;
    }
}
