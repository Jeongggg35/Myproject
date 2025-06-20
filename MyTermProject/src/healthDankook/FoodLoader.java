package healthDankook;

import java.util.*;
import java.io.*;

class FoodLoader {
    public static Map<String, Food> loadFoodData() {
        Map<String, Food> foodMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("food.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length < 6) continue; 
                String name = tokens[0].trim();
                double kcal = Double.parseDouble(tokens[1]);
                double carb = Double.parseDouble(tokens[2]);
                double protein = Double.parseDouble(tokens[3]);
                double fat = Double.parseDouble(tokens[4]);
                int gramPerUnit = Integer.parseInt(tokens[5]);
                foodMap.put(name, new Food(kcal, carb, protein, fat, gramPerUnit));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foodMap;
    }
}