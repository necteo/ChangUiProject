package FoodNutrientManagement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class GetOpenData {
    public static void main(String[] args) {    // 테스트용
        try {
            ArrayList<FoodNutrient> fnList = getData("떡");
            for (FoodNutrient fn: fnList) {
                System.out.println(fn.getName() + " " + fn.getCalories() + " " + fn.getCarbohydrate() + " " + fn.getProtein());
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            System.out.println("결과없음");
        }
    }
    public static ArrayList<FoodNutrient> getData(String foodName) throws IOException, ParseException {
        /*URL*/
        String urlBuilder = "http://openapi.foodsafetykorea.go.kr/api" +
                "/" + "54746e590a1e4427a624" + /*Service Key*/
                "/" + URLEncoder.encode("I2790", StandardCharsets.UTF_8) + /*Service ID*/
                "/" + URLEncoder.encode("json", StandardCharsets.UTF_8) + /*응답데이터 형식(xml/json) Default: xml*/
                "/" + URLEncoder.encode("1", StandardCharsets.UTF_8) + /*요청시작위치*/
                "/" + URLEncoder.encode("8", StandardCharsets.UTF_8) + /*요청종료위치*/
                "/" + URLEncoder.encode("DESC_KOR", StandardCharsets.UTF_8) +
                "=" + URLEncoder.encode(foodName, StandardCharsets.UTF_8); /*식품이름*/
        URL url = new URL(urlBuilder);
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response Msg: " + conn.getResponseMessage() + "Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result = sb.toString();
        System.out.println(result);

        ArrayList<FoodNutrient> foodNtrInfoList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(result);
        JSONObject body = (JSONObject) jsonObj.get("I2790");// response 로 부터 body 찾아오기
        JSONArray row = (JSONArray) body.get("row");
        if (row == null) {
            return null;
        }
        for (Object f : row) {
            FoodNutrient foodNtrInfo = new FoodNutrient();
            JSONObject food = (JSONObject) f;
            foodNtrInfo.setName((String) food.get("DESC_KOR"));
            if (food.get("NUTR_CONT1") == "") {
                foodNtrInfo.setCalories(0);
            } else {
                foodNtrInfo.setCalories(Double.parseDouble((String) food.get("NUTR_CONT1")));
            }
            if (food.get("NUTR_CONT2") == "") {
                foodNtrInfo.setCarbohydrate(0);
            } else {
                foodNtrInfo.setCarbohydrate(Double.parseDouble((String) food.get("NUTR_CONT2")));
            }
            if (food.get("NUTR_CONT3") == "") {
                foodNtrInfo.setProtein(0);
            } else {
                foodNtrInfo.setProtein(Double.parseDouble((String) food.get("NUTR_CONT3")));
            }
            if (food.get("NUTR_CONT4") == "") {
                foodNtrInfo.setFat(0);
            } else {
                foodNtrInfo.setFat(Double.parseDouble((String) food.get("NUTR_CONT4")));
            }
            foodNtrInfoList.add(foodNtrInfo);
        }

        return foodNtrInfoList;
    }

    public static FoodNutrient recommend(String food_cd) throws IOException, ParseException, ParserConfigurationException, SAXException {
        /*URL*/

        String urlBuilder = "http://openapi.foodsafetykorea.go.kr/api/54746e590a1e4427a624/I2790/json/1/1/FOOD_CD=" + food_cd;
        URL url = new URL(urlBuilder);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result = sb.toString();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(result);
        JSONObject body = (JSONObject) jsonObj.get("I2790");// response 로 부터 body 찾아오기
        JSONArray row = (JSONArray) body.get("row");
        JSONObject food = (JSONObject) row.get(0);

        FoodNutrient foodNtrInfo = new FoodNutrient();
        foodNtrInfo.setName((String) food.get("DESC_KOR"));
        if (food.get("NUTR_CONT1") == "") {
            foodNtrInfo.setCalories(0);
        } else {
            foodNtrInfo.setCalories(Double.parseDouble((String) food.get("NUTR_CONT1")));
        }
        if (food.get("NUTR_CONT2") == "") {
            foodNtrInfo.setCarbohydrate(0);
        } else {
            foodNtrInfo.setCarbohydrate(Double.parseDouble((String) food.get("NUTR_CONT2")));
        }
        if (food.get("NUTR_CONT3") == "") {
            foodNtrInfo.setProtein(0);
        } else {
            foodNtrInfo.setProtein(Double.parseDouble((String) food.get("NUTR_CONT3")));
        }
        if (food.get("NUTR_CONT4") == "") {
            foodNtrInfo.setFat(0);
        } else {
            foodNtrInfo.setFat(Double.parseDouble((String) food.get("NUTR_CONT4")));
        }
        return foodNtrInfo;
    }
}