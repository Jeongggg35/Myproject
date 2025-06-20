
package healthDankook;

import java.awt.BorderLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


class ResultPanel extends JPanel { // 사용자의 정보를 바탕으로 목표 칼로리와 영양소 비율을 계산하여 보여주는 화면
    private JLabel infoLabel; // 계산된 칼로리와 영양소비율 보여줄 라벨

    public ResultPanel(MainFrame frame) {
        setLayout(new BorderLayout());

        infoLabel = new JLabel("", 0);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(infoLabel, BorderLayout.CENTER); // 라벨을 화면 중앙에 배치

        JButton nextButton = new JButton("다음");
        nextButton.setFont(nextButton.getFont().deriveFont(18f));
        nextButton.addActionListener(e -> frame.showPanel("daily")); // 음식 기록 패널로 넘어감
        add(nextButton, BorderLayout.SOUTH); // 다음 버튼 하단에 배치

        addComponentListener(new ComponentAdapter() { // 화면 전환 시 자동으로 정보가 계산되어 나오게하기위해 (따로 버튼없이)
            @Override                      // ComponentListener의 모든 메서드를 구현하지 않고 필요한 메서드만 오버라이드하기위해 ComponentAdapter사용
            public void componentShown(ComponentEvent evt) { // ResultPanel이 화면에 보일 때마다 이 코드가 실행
                UserInfo info = frame.getUserInfo(); // 사용자 정보 객체 불러옴

                int height = info.getHeight(); // 사용자가 앞서 입력한 키, 몸무게, 나이, 성별, 목표를 변수에 저장
                int weight = info.getWeight();
                int age = info.getAge();    
                String gender = info.getGender();
                String goal = info.getGoal();
                
                // 기초대사량 계산
                double bmr; 
                if (gender.equals("남자")) {
                    bmr = 66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * age); //해리스-베네딕트 공식에 따라 BMR을 계산
                } 
                else {// 여자
                    bmr = 655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * age);
                }
                
                 // 목표가 다이어트면 BMR의 85%를 목표 칼로리로, 벌크업이면 115%를 목표 칼로리로 설정
                double targetCalories = goal.equals("다이어트") ? bmr * 0.85 : bmr * 1.15; 
                
                // 탄,단,지 5:3:2 비율로
                int carb = (int)(targetCalories * 0.5 / 4); // 탄수화물 칼로리를 전체 목표 칼로리의 50%로 잡고, 1g당 4칼로리 기준으로 g 수 계산
                int protein = (int)(targetCalories * 0.3 / 4);  // 단백질은 30%, 1g당 4칼로리 기준으로 g 수 계산
                int fat = (int)(targetCalories * 0.2 / 9); // 지방은 20%, 1g당 9칼로리 기준으로 g 수 계산

                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // 현재 날짜를 "yyyy-MM-dd" 형식의 문자열로 저장

                info.setTargetCalories((int)targetCalories); // UserInfo 객체에 목표 칼로리 저장
                info.setCarb(carb); // 탄수화물 목표량 저장
                info.setProtein(protein); // 단백질 목표량 저장
                info.setFat(fat); // 지방 목표량 저장
                info.getTodayRecord().setTargets((int)targetCalories, carb, protein, fat);

                infoLabel.setText("<html><div style='text-align: center;'>"
                        + gender + " | " + date + "<br><br>"
                        + "목표 칼로리: " + (int)targetCalories + " kcal<br>"
                        + "탄수화물: " + carb + "g, 단백질: " + protein + "g, 지방: " + fat + "g" + "</div></html>");
            } // 라벨에 HTML 형식으로 사용자 성별, 날짜, 목표 칼로리, 탄단지 목표량을 깔끔하게 출력
        });
    }
}