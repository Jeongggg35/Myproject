
package healthDankook;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class StartPanel extends JPanel {
    public StartPanel(MainFrame frame) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("<건강한 단국인>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 50, 0));
        add(titleLabel, BorderLayout.NORTH);

        JButton startButton = new JButton("처음부터 시작하기");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        startButton.setPreferredSize(new Dimension(200, 50));

        JButton loadButton = new JButton("기록 불러오기");
        loadButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        loadButton.setPreferredSize(new Dimension(200, 40));

        JPanel centerPanel = new JPanel();
        centerPanel.add(startButton);
        centerPanel.add(loadButton);
        add(centerPanel, BorderLayout.CENTER);

        
        startButton.addActionListener(e -> {
            frame.getUserInfo().clearTodayRecord(); 
            frame.showPanel("gender");
        });

     
     loadButton.addActionListener(e -> {
        	System.out.println("불러오기 버튼 클릭됨");
            frame.getUserInfo().loadTodayRecord(); 
            frame.showPanel("daily"); 
        });
        
       

    }
}
