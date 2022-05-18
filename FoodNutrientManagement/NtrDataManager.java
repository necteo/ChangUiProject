package FoodNutrientManagement;

import SystemManagement.DBController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NtrDataManager {       // DB 일일_영양소 테이블의 입출력을 담당하는 클래스
    DBController db = new DBController();

    public ArrayList<DailyNutrient> readData(int[] startInfo, int[] endInfo) {  // DB 에서 기간에 따라 데이터 읽어오는 함수
        ArrayList<DailyNutrient> dnList = new ArrayList<DailyNutrient>();   // 저장 후 리턴용

        try {
            db.dbConn();
            String sql = "select * from 일일_영양소 where 날짜 between ? and ?";
            db.pst = db.conn.prepareStatement(sql);
            db.pst.setString(1, startInfo[0] + "-" + startInfo[1] + "-" +  startInfo[2]);
            db.pst.setString(2, endInfo[0] + "-" + endInfo[1] + "-" +  endInfo[2]);
            db.rs = db.pst.executeQuery();
            while (db.rs.next()) {
                String date = db.rs.getDate("날짜").toString();
                int time = Integer.parseInt(db.rs.getString("시간대"));
                float calories = Float.parseFloat(String.valueOf(db.rs.getDouble("열량")));
                float carbohydrate = Float.parseFloat(String.valueOf(db.rs.getDouble("탄수화물")));
                float protein = Float.parseFloat(String.valueOf(db.rs.getDouble("단백질")));
                float fat = Float.parseFloat(String.valueOf(db.rs.getDouble("지방")));
                DailyNutrient dn = new DailyNutrient(date, time, calories, carbohydrate, protein, fat);
                dnList.add(dn);
            }
            db.rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.dbClose();
        }
        return dnList;
    }

    public void insertData(DailyNutrient dn) {  // DB 에 데이터 저장하는 클래스
        try {
            db.dbConn();
            String sql = "insert into 일일_영양소 values (?, ?, ?, ?, ? ,?)";
            db.pst = db.conn.prepareStatement(sql);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime date = LocalDate.parse(dn.getDate(), formatter).atStartOfDay();
            db.pst.setDate(1, java.sql.Date.valueOf(date.toLocalDate()));
            db.pst.setInt(2, dn.getTime());
            db.pst.setDouble(3, dn.getCalories());
            db.pst.setDouble(4, dn.getCarbohydrate());
            db.pst.setDouble(5, dn.getProtein());
            db.pst.setDouble(6, dn.getFat());
            db.pst.executeUpdate();
            db.dbClose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
