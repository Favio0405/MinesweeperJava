import javax.swing.*;
import java.awt.*;

public class BottomBar extends JPanel{
    private BottomBarListener listener;
    public BottomBar(){
        setLayout(new BorderLayout());
        JButton toMenuButton = new JButton("Difficulty");
        toMenuButton.addActionListener(e -> listener.toMenu());
        JButton scoresButton = new JButton("Best Scores");
        scoresButton.addActionListener(e -> listener.scoreBoard());
        JLabel bestScore = new JLabel();
        add(toMenuButton, BorderLayout.WEST);
        add(scoresButton, BorderLayout.CENTER);
        add(bestScore, BorderLayout.EAST);
    }
    public void setListener(BottomBarListener listener){
        this.listener = listener;
    }
}
