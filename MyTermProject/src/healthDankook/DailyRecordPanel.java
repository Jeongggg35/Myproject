package healthDankook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

class DailyRecordPanel extends JPanel {
    private JLabel summaryLabel;
    private String[] meals = {"아침", "점심", "저녁", "간식"};
    private JComboBox<String>[] foodCombos = new JComboBox[4];
    private JTextField[] countFields = new JTextField[4];
    private JLabel[] mealSummaries = new JLabel[4];
    private JLabel[] searchResultLabels = new JLabel[4];
    private JButton[] addNewFoodButtons = new JButton[4];
    private Map<String, Food> foodData;

    private double totalKcal = 0;
    private double totalCarb = 0;
    private double totalProtein = 0;
    private double totalFat = 0;

    private UserInfo info;
    private MainFrame frame;

    public DailyRecordPanel(MainFrame frame) {
        this.frame = frame;
        this.info = frame.getUserInfo();

        setLayout(new BorderLayout());

        
        summaryLabel = new JLabel("", SwingConstants.CENTER);
        summaryLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        updateSummaryLabel();

        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(summaryLabel, BorderLayout.CENTER);

        JButton viewHistoryButton = new JButton("내 기록 보기");
        viewHistoryButton.addActionListener(e -> frame.showPanel("recordList"));
        topPanel.add(viewHistoryButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 음식 데이터 로드
        foodData = FoodLoader.loadFoodData();

        // 식사별 입력 패널
        JPanel gridPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        for (int i = 0; i < 4; i++) {
            JPanel mealPanel = new JPanel(new GridLayout(4, 3, 5, 5)); 
            mealPanel.setBorder(BorderFactory.createTitledBorder(meals[i]));

            foodCombos[i] = new JComboBox<>(foodData.keySet().toArray(new String[0]));
            foodCombos[i].setEditable(true);
            foodCombos[i].setSelectedItem("");

            countFields[i] = new JTextField();

            JButton addButton = new JButton("추가");
            JButton removeButton = new JButton("삭제");

            int index = i;

            
            mealSummaries[i] = new JLabel("총합: 0kcal / 탄: 0g / 단: 0g / 지: 0g");
            searchResultLabels[i] = new JLabel("");
            addNewFoodButtons[i] = new JButton("직접 추가");
            addNewFoodButtons[i].setVisible(false);

            addNewFoodButtons[i].addActionListener(e -> {
                String newFoodName = ((JTextField) foodCombos[index].getEditor().getEditorComponent()).getText().trim();
                if (newFoodName.isEmpty()) return;

                try {
                    String kcalStr = JOptionPane.showInputDialog(this, newFoodName + " 칼로리(kcal) 입력:");
                    if (kcalStr == null) return;
                    double kcal = Double.parseDouble(kcalStr);

                    String carbStr = JOptionPane.showInputDialog(this, newFoodName + " 탄수화물(g) 입력:");
                    if (carbStr == null) return;
                    double carb = Double.parseDouble(carbStr);

                    String proteinStr = JOptionPane.showInputDialog(this, newFoodName + " 단백질(g) 입력:");
                    if (proteinStr == null) return;
                    double protein = Double.parseDouble(proteinStr);

                    String fatStr = JOptionPane.showInputDialog(this, newFoodName + " 지방(g) 입력:");
                    if (fatStr == null) return;
                    double fat = Double.parseDouble(fatStr);

                    String gramStr = JOptionPane.showInputDialog(this, newFoodName + " 1개당 무게(g) 입력:");
                    if (gramStr == null) return;
                    int gram = Integer.parseInt(gramStr);

                    addFoodToFile(newFoodName, kcal, carb, protein, fat, gram);

                    foodData = FoodLoader.loadFoodData();
                    for (JComboBox<String> combo : foodCombos) {
                        combo.setModel(new DefaultComboBoxModel<>(foodData.keySet().toArray(new String[0])));
                        combo.setEditable(true);
                    }

                    searchResultLabels[index].setText("");
                    addNewFoodButtons[index].setVisible(false);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "숫자를 올바르게 입력해주세요.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "파일 쓰기 오류: " + ex.getMessage());
                }
            });

            Component editorComp = foodCombos[i].getEditor().getEditorComponent();
            if (editorComp instanceof JTextField) {
                JTextField editor = (JTextField) editorComp;
                editor.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        String input = editor.getText().trim();
                        if (input.isEmpty()) {
                            searchResultLabels[index].setText("");
                            addNewFoodButtons[index].setVisible(false);
                            return;
                        }
                        if (foodData.containsKey(input)) {
                            searchResultLabels[index].setText("");
                            addNewFoodButtons[index].setVisible(false);
                        } else {
                            searchResultLabels[index].setText("유효한 음식이 없습니다.");
                            addNewFoodButtons[index].setVisible(true);
                        }
                    }
                });
            }

            
            addButton.addActionListener(e -> {
                String selectedFood = (String) foodCombos[index].getEditor().getItem();
                String countText = countFields[index].getText().trim();
                try {
                    int count = Integer.parseInt(countText);
                    if (count <= 0) throw new NumberFormatException();

                    Food food = foodData.get(selectedFood);
                    if (food == null) {
                        JOptionPane.showMessageDialog(this, "음식을 선택하거나 올바르게 입력해주세요.");
                        return;
                    }

                    
                    info.getTodayRecord().addFood(meals[index], selectedFood, count);

                    
                    refreshFromTodayRecord();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "유효한 개수를 입력하세요.");
                }
            });

            
            removeButton.addActionListener(e -> {
                String selectedFood = (String) foodCombos[index].getEditor().getItem();
                String countText = countFields[index].getText().trim();
                try {
                    int count = Integer.parseInt(countText);
                    if (count <= 0) throw new NumberFormatException();

                    Food food = foodData.get(selectedFood);
                    if (food == null) {
                        JOptionPane.showMessageDialog(this, "음식을 선택하거나 올바르게 입력해주세요.");
                        return;
                    }

                  
                    info.getTodayRecord().removeFood(meals[index], selectedFood, count);

                   
                    refreshFromTodayRecord();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "유효한 개수를 입력하세요.");
                }
            });

            mealPanel.add(new JLabel("음식 선택:"));
            mealPanel.add(foodCombos[i]);
            mealPanel.add(addNewFoodButtons[i]);
            mealPanel.add(new JLabel("몇 개 먹었나요?"));
            mealPanel.add(countFields[i]);
            mealPanel.add(addButton);
            mealPanel.add(removeButton);
            mealPanel.add(mealSummaries[i]);
            mealPanel.add(searchResultLabels[i]);

            gridPanel.add(mealPanel);
        }

        add(gridPanel, BorderLayout.CENTER);
    }

    private void updateSummaryLabel() {
        int targetCal = info.getTargetCalories() > 0 ? info.getTargetCalories() : 2000;
        int targetCarb = info.getCarb() > 0 ? info.getCarb() : 180;
        int targetProtein = info.getProtein() > 0 ? info.getProtein() : 120;
        int targetFat = info.getFat() > 0 ? info.getFat() : 40;

        String calExceed = totalKcal > targetCal ? " <span style='color:red;'>초과!</span>" : "";
        String carbExceed = totalCarb > targetCarb ? " <span style='color:red;'>초과!</span>" : "";
        String proteinExceed = totalProtein > targetProtein ? " <span style='color:red;'>초과!</span>" : "";
        String fatExceed = totalFat > targetFat ? " <span style='color:red;'>초과!</span>" : "";

        summaryLabel.setText(String.format(
                "<html><div style='text-align:center;'>"
                        + "칼로리: %.0f / %d kcal%s<br>"
                        + "탄수화물: %.1f / %d g%s<br>"
                        + "단백질: %.1f / %d g%s<br>"
                        + "지방: %.1f / %d g%s"
                        + "</div></html>",
                totalKcal, targetCal, calExceed,
                totalCarb, targetCarb, carbExceed,
                totalProtein, targetProtein, proteinExceed,
                totalFat, targetFat, fatExceed
        ));
    }

    private void addFoodToFile(String name, double kcal, double carb, double protein, double fat, int gram) throws IOException {
        try (FileWriter fw = new FileWriter("food.txt", true)) {
            fw.write(String.format("\n%s,%.1f,%.1f,%.1f,%.1f,%d", name, kcal, carb, protein, fat, gram));
        }
    }

    public void refreshFromTodayRecord() {
        totalKcal = 0;
        totalCarb = 0;
        totalProtein = 0;
        totalFat = 0;

        FoodRecord record = info.getTodayRecord();
        if (record != null) {
            for (int i = 0; i < meals.length; i++) {
                String meal = meals[i];
                Map<String, Integer> items = record.getMealRecords(meal);
                double mealKcal = 0, mealCarb = 0, mealProtein = 0, mealFat = 0;
                for (Map.Entry<String, Integer> entry : items.entrySet()) {
                    Food food = foodData.get(entry.getKey());
                    if (food != null) {
                        int count = entry.getValue();
                        int gram = food.gramPerUnit * count;
                        mealKcal += food.kcal * gram / 100.0;
                        mealCarb += food.carb * gram / 100.0;
                        mealProtein += food.protein * gram / 100.0;
                        mealFat += food.fat * gram / 100.0;
                    }
                }
                totalKcal += mealKcal;
                totalCarb += mealCarb;
                totalProtein += mealProtein;
                totalFat += mealFat;

                mealSummaries[i].setText(String.format(
                        "총합: %.0fkcal / 탄: %.1fg / 단: %.1fg / 지: %.1fg",
                        mealKcal, mealCarb, mealProtein, mealFat));
            }
        }
        updateSummaryLabel();
    }
}

