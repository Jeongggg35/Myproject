package healthDankook;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class GenderPanel extends JPanel {
    public GenderPanel(MainFrame frame) {
        setLayout(new GridLayout(3, 1, 10, 10)); // 3행 1열 행열간 간격 10
        setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50)); // 패널 안쪽 여백설정 (버튼크기 너무크지않게하기위해)

        JLabel genderLabel = new JLabel("성별을 선택하세요", 0);
        genderLabel.setFont(genderLabel.getFont().deriveFont(20f));
        add(genderLabel); // 라벨을 패널에 추가

        JButton maleButton = new JButton("남자");
        maleButton.setFont(maleButton.getFont().deriveFont(24f));
        add(maleButton);

        JButton femaleButton = new JButton("여자");
        femaleButton.setFont(femaleButton.getFont().deriveFont(24f));
        add(femaleButton);

        maleButton.addActionListener(e -> {
            frame.getUserInfo().setGender("남자"); // MainFrame이 가지고 있는 UserInfo 객체에 남자라고 저장.
            frame.showPanel("body"); // 다음 화면인 body 패널(키, 몸무게 입력 화면)으로 이동.
        });

        femaleButton.addActionListener(e -> {
            frame.getUserInfo().setGender("여자");
            frame.showPanel("body");
        });
    }
}