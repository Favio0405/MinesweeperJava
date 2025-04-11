import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ScoreBoard extends JPanel{
    private final ScoreList scores;
    private final JPanel scoresPanel;
    //index 0 = easy   1 = medium   2 = hard
    private final JPanel[] scoreDisplays;
    CardLayout cards;
    public ScoreBoard(){
        super();
        scores = ScoreList.loadFromFile();
        cards = new CardLayout();
        scoresPanel = new JPanel(cards);
        scoreDisplays = new JPanel[3];

        setLayout(new BorderLayout());
        add(scoresPanel, BorderLayout.SOUTH);

        initializeScores();

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton easy = new JButton("Easy");
        JButton medium = new JButton("Medium");
        JButton hard = new JButton("Hard");
        buttonPanel.add(easy);
        buttonPanel.add(medium);
        buttonPanel.add(hard);
        add(buttonPanel, BorderLayout.NORTH);
        easy.addActionListener(e -> cards.show(scoresPanel, "EASY"));
        medium.addActionListener(e -> cards.show(scoresPanel, "MEDIUM"));
        hard.addActionListener(e -> cards.show(scoresPanel, "HARD"));
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
}
