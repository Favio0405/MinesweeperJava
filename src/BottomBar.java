import javax.swing.*;
import java.awt.*;

public class BottomBar extends JPanel{
    private final JButton toMenuButton;
    private final JButton scoresButton;
    private final JLabel bestScore;
    private BottomBarListener listener;
    public BottomBar(){
        setLayout(new BorderLayout());
        toMenuButton = new JButton("Difficulty");
        toMenuButton.addActionListener(e -> {listener.toMenu();});
        scoresButton = new JButton("Best Scores");
        bestScore = new JLabel();
        add(toMenuButton, BorderLayout.WEST);
        add(scoresButton, BorderLayout.CENTER);
        add(bestScore, BorderLayout.EAST);
    }
    public void setListener(BottomBarListener listener){
        this.listener = listener;
    }
}
