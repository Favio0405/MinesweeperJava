import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;

public class ScoreBoard extends JPanel implements ActionListener {
    private final ScoreList scores;
    private final JPanel buttonPanel;
    private final JButton easy;
    private final JButton medium;
    private final JButton hard;
    private final CardLayout cards;
    private final JPanel scoresPanel;
    //index 0 = easy   1 = medium   2 = hard
    private final JPanel[] scoreDisplays;
    public ScoreBoard(){
        super();
        scores = ScoreList.loadFromFile();
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        easy = new JButton("Easy");
        medium = new JButton("Medium");
        hard = new JButton("Hard");
        cards = new CardLayout();
        scoresPanel = new JPanel(cards);
        scoreDisplays = new JPanel[3];

        buttonPanel.add(easy);
        buttonPanel.add(medium);
        buttonPanel.add(hard);

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(scoresPanel, BorderLayout.SOUTH);

        initializeScores();
    }

    private void initializeScores(){
        scoreDisplays[0] = new JPanel();
        scoreDisplays[0].setLayout(new BoxLayout(scoreDisplays[0], BoxLayout.Y_AXIS));
        scoreDisplays[1] = new JPanel();
        scoreDisplays[1].setLayout(new BoxLayout(scoreDisplays[1], BoxLayout.Y_AXIS));
        scoreDisplays[2] = new JPanel();
        scoreDisplays[2].setLayout(new BoxLayout(scoreDisplays[2], BoxLayout.Y_AXIS));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        int i = 0;
        while(i < 3){
            int j = 0;
            int count = 0;
            while(scores.scoreList[i][j] != null){
                Score score = scores.scoreList[i][j];
                String str = count + "   " + score.date().format(formatter) + "      " + score.score();
                scoreDisplays[i].add(new JLabel(str));
                count++;
                j++;
            }
            if(count == 0) scoreDisplays[i].add(new JLabel("             No scores available"));
            i++;
        }
        scoresPanel.removeAll();
        scoresPanel.add(scoreDisplays[0], "EASY");
        scoresPanel.add(scoreDisplays[1], "MEDIUM");
        scoresPanel.add(scoreDisplays[2], "HARD");
    }

    public void addScore(Score score){
        scores.addScore(score);
        scores.saveToFile();
        initializeScores();
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
