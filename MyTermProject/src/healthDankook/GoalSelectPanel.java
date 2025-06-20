package healthDankook;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class GoalSelectPanel extends JPanel {
    public GoalSelectPanel(MainFrame frame) {
        setLayout(new GridLayout(3, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        JLabel label = new JLabel("목표를 선택하세요", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(18f));
        add(label);

        JButton dietButton = new JButton("다이어트");
        dietButton.setFont(dietButton.getFont().deriveFont(18f));
        add(dietButton);

        JButton bulkButton = new JButton("벌크업");
        bulkButton.setFont(bulkButton.getFont().deriveFont(18f));
        add(bulkButton);

        dietButton.addActionListener(e -> {
            frame.getUserInfo().setGoal("다이어트");
            calculateAndSetTargets(frame.getUserInfo());
            frame.showPanel("daily");  // result 대신 daily로 바로 이동
        });

        bulkButton.addActionListener(e -> {
            frame.getUserInfo().setGoal("벌크업");
            calculateAndSetTargets(frame.getUserInfo());
            frame.showPanel("daily");
        });

    }

    private void calculateAndSetTargets(UserInfo info) {
        double bmr;
        if ("남자".equals(info.getGender())) {
            bmr = 66.47 + (13.75 * info.getWeight()) + (5.003 * info.getHeight()) - (6.755 * info.getAge());
        } else {
            bmr = 655.1 + (9.563 * info.getWeight()) + (1.850 * info.getHeight()) - (4.676 * info.getAge());
        }

        double targetCalories = info.getGoal().equals("다이어트") ? bmr * 0.85 : bmr * 1.15;

        info.setTargetCalories((int) targetCalories);
        info.setCarb((int) (targetCalories * 0.5 / 4));
        info.setProtein((int) (targetCalories * 0.3 / 4));
        info.setFat((int) (targetCalories * 0.2 / 9));
    }
}

