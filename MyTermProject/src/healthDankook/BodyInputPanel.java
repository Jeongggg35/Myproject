package healthDankook;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

class BodyInputPanel extends JPanel { 
    public BodyInputPanel(MainFrame frame) {
        setLayout(new GridLayout(6, 1, 10, 10)); 
        setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        JLabel label = new JLabel("키(cm) 몸무게(kg) 나이(age)를 순서대로 입력", SwingConstants.CENTER);
        add(label); 

        JTextField heightField = new JTextField();
        add(heightField);

        JTextField weightField = new JTextField();
        add(weightField);
        
        JTextField ageField = new JTextField();
        add(ageField);

        JButton nextButton = new JButton("다음");
        add(nextButton);

        JLabel errorLabel = new JLabel("", 0);
        errorLabel.setForeground(Color.RED);
        add(errorLabel);

        nextButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                try {
                    int height = Integer.parseInt(heightField.getText().trim()); 
                    int weight = Integer.parseInt(weightField.getText().trim());
                    int age = Integer.parseInt(ageField.getText().trim());

                    if (height <= 0 || weight <= 0 || age <= 0) throw new NumberFormatException(); // 음수 또는 0 입력시 예외 발생

                    frame.getUserInfo().setHeight(height); // MainFrame에서 UserInfo 객체를 가져와서 입력받은 키와 몸무게를 저장
                    frame.getUserInfo().setWeight(weight); 
                    frame.getUserInfo().setAge(age); 
                  

                    frame.showPanel("goal"); // 입력이 잘 되면 다음 패널로 이동
                } catch (NumberFormatException ex) {
                    errorLabel.setText("유효한 숫자를 입력하세요."); //입력값이 숫자가 아니거나 유효하지 않은 경우, 빨간색 오류 메시지
                }
            }
        });
    }
}