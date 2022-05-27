package UserManagement;

import SystemManagement.DBController;

public class UserInfoManager {  // DB user_info 테이블의 입출력을 담당하는 클래스
    DBController db = new DBController();

    public boolean setInfo(String id, String pwd, String name, int year, int sex) {    // 사용자 정보를 DB 에 저장
        if (isDuplicate(pwd))   // 중복 확인
            return false;       // 중복일 시 false 리턴 후 함수 종료
        try {
            db.dbConn();
            String sql = "insert into user_info values (?, ?, ?, ?, ?)";
            db.pst = db.conn.prepareStatement(sql);
            db.pst.setString(1, id);
            db.pst.setString(2, pwd);
            db.pst.setString(3, name);
            db.pst.setInt(4, year);
            db.pst.setInt(5, sex);
            db.pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.dbClose();
        }
        return true;
    }

    public String getInfo(String i, String p) { // name, password 데이터를 읽어옴
        String id = "";
        String pwd = "";
        try {
            db.dbConn();
            String sql = "select id, password from user_info where id=? and password=?";
            db.pst = db.conn.prepareStatement(sql);
            db.pst.setString(1, i);
            db.pst.setString(2, p);
            db.rs = db.pst.executeQuery();
            while (db.rs.next()) {
                id = db.rs.getString("id");
                pwd = db.rs.getString("password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.dbClose();
        }
        return id + "," + pwd;    // name, pwd 형태
    }

    public boolean isDuplicate(String id) {    // 중복 확인
        boolean isDup = false;
        try {
            db.dbConn();
            String sql = "select id from user_info where id=?";
            db.pst = db.conn.prepareStatement(sql);
            db.pst.setString(1, id);
            db.rs = db.pst.executeQuery();
            if (db.rs.next())
                isDup = true;   //  결과값이 있으면 중복
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.dbClose();
        }
        return isDup;
    }

    public void controlLoginState(String id, int state) {
        try {
            db.dbConn();
            if (state == 0) {
                String sql = "insert into login_state values (?)";
                db.pst = db.conn.prepareStatement(sql);
                db.pst.setString(1, id);
                db.pst.executeUpdate();
            } else if (state == 1) {
                String sql = "delete from login_state where id=?";
                db.pst = db.conn.prepareStatement(sql);
                db.pst.setString(1, id);
                db.pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.dbClose();
        }
    }
}
