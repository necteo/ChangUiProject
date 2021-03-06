package SystemManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBController { // DB 연결을 위한 클래스
    public Connection conn = null;
    public Statement st = null;
    public PreparedStatement pst = null;
    public ResultSet rs = null;

    public DBController(){}

    // �����ͺ��̽��� �����Ѵ�.
    public void dbConn() {  // DB 연결

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/daily_nutrient?serverTimezone=UTC";
            String id = "root";
            String pwd = "root";

            conn = DriverManager.getConnection(url, id, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // �����ͺ��̽��� �ݴ´�.
    public void dbClose(){  // DB 연결 종료
        try{
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (pst != null) pst.close();
            if (conn != null) conn.close();
            System.out.println("Disconnection Succeeded.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}