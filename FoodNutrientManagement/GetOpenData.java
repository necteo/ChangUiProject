package FoodNutrientManagement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class GetOpenData {
    public static FoodNutrient getData(String foodName) throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1/getFoodNtrItdntList1"); /*URL*/
        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", "UTF-8")).append("=ditvr6RhuH%2F8YEdJMyyqaCM7wYxAgA2k8uJgwIerCCPwNnXfilpxoemvXEaZH%2BdIs1CFqKGaHV60SMLZkqhRNA%3D%3D"); /*Service Key*/
        urlBuilder.append("&").append(URLEncoder.encode("pageNo", "UTF-8")).append("=").append(URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&").append(URLEncoder.encode("numOfRows", "UTF-8")).append("=").append(URLEncoder.encode("1", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&").append(URLEncoder.encode("desc_kor", "UTF-8")).append("=").append(URLEncoder.encode(foodName, "UTF-8")); /*식품이름*/
        urlBuilder.append("&").append(URLEncoder.encode("type", "UTF-8")).append("=").append(URLEncoder.encode("json", "UTF-8")); /*응답데이터 형식(xml/json) Default: xml*/
        URL url = new URL(urlBuilder.toString());
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
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

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(result);
        JSONObject body = (JSONObject) jsonObj.get("body");// response 로 부터 body 찾아오기
        JSONArray items = (JSONArray) body.get("items");
        JSONObject item = (JSONObject) items.get(0);

        FoodNutrient foodNtrInfo = new FoodNutrient();
        foodNtrInfo.setName((String) item.get("DESC_KOR"));
        foodNtrInfo.setCalories((Double) item.get("NUTR_CONT1"));
        foodNtrInfo.setCarbohydrate((Double) item.get("NUTR_CONT2"));
        foodNtrInfo.setProtein((Double) item.get("NUTR_CONT3"));
        foodNtrInfo.setFat((Double) item.get("NUTR_CONT4"));

        return foodNtrInfo;
    }
}

