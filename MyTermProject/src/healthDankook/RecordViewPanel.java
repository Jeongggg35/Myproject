package healthDankook;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.time.LocalDate;
import javax.swing.JOptionPane;


public class RecordViewPanel extends JPanel {
    public RecordViewPanel(MainFrame frame) {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("← 뒤로가기");
        backButton.addActionListener(e -> frame.showPanel("daily"));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("📋 내 식사 기록", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(topPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.setBackground(Color.WHITE);

       
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        JLabel dateLabel = new JLabel(dateStr, SwingConstants.CENTER);
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        dateLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        contentPanel.add(dateLabel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 

        FoodRecord todayRecord = frame.getUserInfo().getTodayRecord();
        Map<String, Food> foodData = FoodLoader.loadFoodData();

        double totalKcal = 0;
        double totalCarb = 0;
        double totalProtein = 0;
        double totalFat = 0;

        for (String meal : new String[]{"아침", "점심", "저녁", "간식"}) {
            Map<String, Integer> items = todayRecord.getMealRecords(meal);

            double mealKcal = 0, mealCarb = 0, mealProtein = 0, mealFat = 0;

            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                Food food = foodData.get(entry.getKey());
                int count = entry.getValue();
                if (food != null) {
                    int totalGram = food.gramPerUnit * count;
                    mealKcal += food.kcal * totalGram / 100.0;
                    mealCarb += food.carb * totalGram / 100.0;
                    mealProtein += food.protein * totalGram / 100.0;
                    mealFat += food.fat * totalGram / 100.0;
                }
            }

            totalKcal += mealKcal;
            totalCarb += mealCarb;
            totalProtein += mealProtein;
            totalFat += mealFat;

            JPanel mealPanel = new JPanel();
            mealPanel.setLayout(new BoxLayout(mealPanel, BoxLayout.Y_AXIS));
            mealPanel.setBackground(new Color(245, 245, 245));
            mealPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY)
            ));

            JLabel mealTitle = new JLabel(String.format("[%s] 총합: %.0fkcal / 탄: %.1fg / 단: %.1fg / 지: %.1fg", meal, mealKcal, mealCarb, mealProtein, mealFat));
            mealTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
            mealPanel.add(mealTitle);
            mealPanel.add(Box.createVerticalStrut(5));

            if (items.isEmpty()) {
                JLabel noneLabel = new JLabel("  기록 없음");
                noneLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                mealPanel.add(noneLabel);
            } else {
                for (Map.Entry<String, Integer> entry : items.entrySet()) {
                    String foodName = entry.getKey();
                    int count = entry.getValue();
                    Food food = foodData.get(foodName);
                    if (food != null) {
                        int totalGram = food.gramPerUnit * count;
                        double kcal = food.kcal * totalGram / 100.0;
                        double carb = food.carb * totalGram / 100.0;
                        double protein = food.protein * totalGram / 100.0;
                        double fat = food.fat * totalGram / 100.0;

                        JLabel itemLabel = new JLabel(String.format(
                                "  %s - %d개 (총합: %.0fkcal / 탄: %.1fg / 단: %.1fg / 지: %.1fg)",
                                foodName, count, kcal, carb, protein, fat
                        ));
                        itemLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                        mealPanel.add(itemLabel);
                    }
                }
            }
            
            

            mealPanel.add(Box.createVerticalStrut(5));
            contentPanel.add(mealPanel);
            contentPanel.add(Box.createVerticalStrut(15));
            
            JButton exportButton = new JButton("기록 내보내기");
            exportButton.addActionListener(e -> exportTodayRecordToFile(frame));
            topPanel.add(exportButton, BorderLayout.EAST);  

        }

      
        JLabel totalLabel = new JLabel(String.format("총합: %.0fkcal / 탄: %.1fg / 단: %.1fg / 지: %.1fg",
                totalKcal, totalCarb, totalProtein, totalFat));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        contentPanel.add(totalLabel);

        add(scrollPane, BorderLayout.CENTER);
        
        
    }
    private void exportTodayRecordToFile(MainFrame frame) {
        FoodRecord todayRecord = frame.getUserInfo().getTodayRecord();
        Map<String, Food> foodData = FoodLoader.loadFoodData();

        StringBuilder sb = new StringBuilder();
        sb.append("📅 날짜: ").append(LocalDate.now()).append("\n\n");

        for (String meal : new String[]{"아침", "점심", "저녁", "간식"}) {
            sb.append("[").append(meal).append("]\n");
            Map<String, Integer> items = todayRecord.getMealRecords(meal);
            if (items.isEmpty()) {
                sb.append("  기록 없음\n");
            } else {
                for (Map.Entry<String, Integer> entry : items.entrySet()) {
                    String foodName = entry.getKey();
                    int count = entry.getValue();
                    Food food = foodData.get(foodName);
                    if (food != null) {
                        int totalGram = food.gramPerUnit * count;
                        double kcal = food.kcal * totalGram / 100.0;
                        double carb = food.carb * totalGram / 100.0;
                        double protein = food.protein * totalGram / 100.0;
                        double fat = food.fat * totalGram / 100.0;

                        sb.append(String.format("  %s - %d개 (%.0fkcal / 탄: %.1fg / 단: %.1fg / 지: %.1fg)\n",
                                foodName, count, kcal, carb, protein, fat));
                    }
                }
            }
            sb.append("\n");
        }

        String fileName = LocalDate.now() + ".txt";
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.print(sb.toString());
            JOptionPane.showMessageDialog(this, "기록이 '" + fileName + "' 파일로 저장되었습니다.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "저장 실패: " + e.getMessage());
        }
    }

}