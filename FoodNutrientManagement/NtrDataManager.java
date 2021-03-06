package FoodNutrientManagement;

import SystemManagement.DBController;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class NtrDataManager {       // DB 일일_영양소 테이블의 입출력을 담당하는 클래스
    DBController db = new DBController();

    public ArrayList<DailyNutrient> readData(int[] startDate, int[] endDate, String id) {  // DB 에서 기간에 따라 데이터 읽어오는 메소드
        ArrayList<DailyNutrient> dnList = new ArrayList<>();   // 저장 후 리턴용

        try {
            db.dbConn();
            String sql = "select * from 일일_영양소 where id = ? and 날짜 between ? and ?";
            db.pst = db.conn.prepareStatement(sql);
            db.pst.setString(1, id);
            db.pst.setString(2, startDate[0] + "-" + startDate[1] + "-" +  startDate[2]);
            db.pst.setString(3, endDate[0] + "-" + endDate[1] + "-" +  endDate[2]);
            db.rs = db.pst.executeQuery();
            while (db.rs.next()) {
                String date = db.rs.getDate("날짜").toString();
                int time = Integer.parseInt(db.rs.getString("시간대"));
                float calories = Float.parseFloat(String.valueOf(db.rs.getDouble("열량")));
                float carbohydrate = Float.parseFloat(String.valueOf(db.rs.getDouble("탄수화물")));
                float protein = Float.parseFloat(String.valueOf(db.rs.getDouble("단백질")));
                float fat = Float.parseFloat(String.valueOf(db.rs.getDouble("지방")));
                DailyNutrient dn = new DailyNutrient(date, time, calories, carbohydrate, protein, fat, id);
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

    public void insertData(DailyNutrient dn) throws SQLException {  // DB 에 데이터 저장하는 메소드
        if (dn == null) return;
        db.dbConn();
        String sql = "insert into 일일_영양소 values (?, ?, ?, ?, ? ,?, ?)";
        db.pst = db.conn.prepareStatement(sql);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime date = LocalDate.parse(dn.getDate(), formatter).atStartOfDay();
        db.pst.setDate(1, java.sql.Date.valueOf(date.toLocalDate()));
        db.pst.setInt(2, dn.getTime());
        db.pst.setDouble(3, dn.getCalories() * 2);
        db.pst.setDouble(4, dn.getCarbohydrate() * 2);
        db.pst.setDouble(5, dn.getProtein() * 2);
        db.pst.setDouble(6, dn.getFat() * 2);
        db.pst.setString(7, dn.getId());
        db.pst.executeUpdate();
        db.dbClose();
    }

    public String getFoodCD(int n) {
        db.dbConn();
        String sql = "select food_cd from foods where id = ?";
        String code = null;
        try {
            db.pst = db.conn.prepareStatement(sql);
            db.pst.setInt(1, n);
            db.rs = db.pst.executeQuery();
            db.rs.next();
            code = db.rs.getString("food_cd");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            db.dbClose();
        }
        return code;
    }

    public String getFoodCD(String foodName) {
        db.dbConn();
        String sql = "select food_cd from foods where desc_kor = ?";
        String code = null;
        try {
            db.pst = db.conn.prepareStatement(sql);
            db.pst.setString(1, foodName);
            db.rs = db.pst.executeQuery();
            if (db.rs.next()) {
                code = db.rs.getString("food_cd");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.dbClose();
        }
        return code;
    }

    public LimitNutrient getLimit(int sex, int age) {
        LimitNutrient limitNtr = null;
        try {
            db.dbConn();
            String sql = "select * from limit_nutrient where sex=? and age>=?";
            db.pst = db.conn.prepareStatement(sql);
            db.pst.setInt(1, sex);
            db.pst.setInt(2, age);
            db.rs = db.pst.executeQuery();
            db.rs.next();
            limitNtr = new LimitNutrient(sex, age,
                    db.rs.getInt("calorie_limit"),
                    db.rs.getInt("carb_limit"),
                    db.rs.getInt("protein_limit"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.dbClose();
        }

        return limitNtr;
    }
}
