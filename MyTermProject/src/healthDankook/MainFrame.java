

package healthDankook;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private UserInfo userInfo;

    private DailyRecordPanel dailyRecordPanel;

    public MainFrame() {
        setTitle("건강한 단국인");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); 
        setSize(400, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        userInfo = new UserInfo();
      

        StartPanel startPanel = new StartPanel(this);
        GenderPanel genderPanel = new GenderPanel(this);
        BodyInputPanel bodyInputPanel = new BodyInputPanel(this);
        GoalSelectPanel goalSelectPanel = new GoalSelectPanel(this);
        ResultPanel resultPanel = new ResultPanel(this);
        dailyRecordPanel = new DailyRecordPanel(this);

        mainPanel.add(startPanel, "start");
        mainPanel.add(genderPanel, "gender");
        mainPanel.add(bodyInputPanel, "body");
        mainPanel.add(goalSelectPanel, "goal");
        mainPanel.add(resultPanel, "result");
        mainPanel.add(dailyRecordPanel, "daily");

        add(mainPanel);
        setVisible(true);

      
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                userInfo.saveTodayRecord();
                System.exit(0);
            }
        });
    }

    public void showPanel(String name) {
        if ("daily".equals(name)) {
            dailyRecordPanel.refreshFromTodayRecord();
        } else if ("recordList".equals(name)) {
            RecordViewPanel recordPanel = new RecordViewPanel(this);
            mainPanel.add(recordPanel, "recordList");
        }
        cardLayout.show(mainPanel, name);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
