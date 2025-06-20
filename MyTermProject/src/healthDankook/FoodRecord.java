package healthDankook;

import java.util.*;
import org.json.JSONObject;

public class FoodRecord {
    private Map<String, Map<String, Integer>> mealRecords = new HashMap<>();
    
    
    private int targetCalories;
    private int targetCarb;
    private int targetProtein;
    private int targetFat;

    public FoodRecord() {
        mealRecords.put("아침", new HashMap<>());
        mealRecords.put("점심", new HashMap<>());
        mealRecords.put("저녁", new HashMap<>());
        mealRecords.put("간식", new HashMap<>());
    }

    public void addFood(String meal, String foodName, int count) {
        Map<String, Integer> mealMap = mealRecords.get(meal);
        mealMap.put(foodName, mealMap.getOrDefault(foodName, 0) + count);
    }

    public Map<String, Integer> getMealRecords(String meal) {
        return mealRecords.getOrDefault(meal, new HashMap<>());
    }

   
    public void setTargets(int kcal, int carb, int protein, int fat) {
        this.targetCalories = kcal;
        this.targetCarb = carb;
        this.targetProtein = protein;
        this.targetFat = fat;
    }
    
    

    public int getTargetCalories() { return targetCalories; }
    public int getTargetCarb() { return targetCarb; }
    public int getTargetProtein() { return targetProtein; }
    public int getTargetFat() { return targetFat; }

   
    public String toJson() {
        JSONObject obj = new JSONObject();

        for (String meal : mealRecords.keySet()) {
            obj.put(meal, mealRecords.get(meal));
        }

        obj.put("targetCalories", targetCalories);
        obj.put("targetCarb", targetCarb);
        obj.put("targetProtein", targetProtein);
        obj.put("targetFat", targetFat);

        return obj.toString();
    }

   
    public static FoodRecord fromJson(String json) {
        FoodRecord record = new FoodRecord();
        JSONObject obj = new JSONObject(json);

        for (String meal : Arrays.asList("아침", "점심", "저녁", "간식")) {
            if (obj.has(meal)) {
                JSONObject mealObj = obj.getJSONObject(meal);
                for (String food : mealObj.keySet()) {
                    record.addFood(meal, food, mealObj.getInt(food));
                }
            }
        }

        if (obj.has("targetCalories")) record.targetCalories = obj.getInt("targetCalories");
        if (obj.has("targetCarb")) record.targetCarb = obj.getInt("targetCarb");
        if (obj.has("targetProtein")) record.targetProtein = obj.getInt("targetProtein");
        if (obj.has("targetFat")) record.targetFat = obj.getInt("targetFat");

        return record;
    }
    public void removeFood(String meal, String foodName, int count) {
        Map<String, Integer> mealMap = mealRecords.get(meal);
        if (mealMap == null) return;

        int currentCount = mealMap.getOrDefault(foodName, 0);
        int newCount = currentCount - count;

        if (newCount > 0) {
            mealMap.put(foodName, newCount);
        } else {
            mealMap.remove(foodName);
        }
    }

}

