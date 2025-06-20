package healthDankook;
 

import java.util.*;
import org.json.JSONObject;
import java.io.*;
import java.time.LocalDate;


class UserInfo {
    private String gender;
    private int height;
    private int weight;
    private int age;
    private String goal;
    private int targetCalories;
    private int carb;
    private int protein;
    private int fat;
    

    private FoodRecord todayRecord = new FoodRecord();

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setTargetCalories(int targetCalories) {
        this.targetCalories = targetCalories;
    }

    public void setCarb(int carb) {
        this.carb = carb;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public String getGender() {
        return gender;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getAge() {
        return age;
    }

    public String getGoal() {
        return goal;
    }

    public int getTargetCalories() {
        return targetCalories;
    }

    public int getCarb() {
        return carb;
    }

    public int getProtein() {
        return protein;
    }

    public int getFat() {
        return fat;
    }

    public FoodRecord getTodayRecord() {
        return todayRecord;
    }

    public void clearTodayRecord() {
        todayRecord = new FoodRecord();
    }

    
  public void saveTodayRecord() {
        try (PrintWriter out = new PrintWriter("today_record.json")) {
            JSONObject obj = new JSONObject();
            obj.put("todayRecord", new JSONObject(todayRecord.toJson()));
            obj.put("targetCalories", targetCalories);
            obj.put("carb", carb);
            obj.put("protein", protein);
            obj.put("fat", fat);

            out.print(obj.toString(2)); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




   
    public void loadTodayRecord() {
        File file = new File("today_record.json");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
          
            JSONObject obj = new JSONObject(json.toString());

          
            JSONObject recordJson = obj.getJSONObject("todayRecord");
            todayRecord = FoodRecord.fromJson(recordJson.toString());

           
            this.targetCalories = obj.optInt("targetCalories", 2000);
            this.carb = obj.optInt("carb", 180);
            this.protein = obj.optInt("protein", 120);
            this.fat = obj.optInt("fat", 40);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
        
    }
    

    
