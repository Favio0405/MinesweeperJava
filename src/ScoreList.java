import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class ScoreList implements Serializable{
    //[0][i] easy difficulty [1][i] medium [2][i] hard;
    public final Score[][] scoreList;

    public ScoreList(){
        scoreList = new Score[3][10];
    }
    public void addScore(Score score){
        int index =
        switch(score.difficulty()){
            case "EASY" -> 0;
            case "MEDIUM" -> 1;
            case "HARD" -> 2;
            default -> -1;
        };
        boolean wasAdded = false;
        for(int i = 0; i < 10; i++){
            if(scoreList[index][i] == null){
                scoreList[index][i] = score;
                wasAdded = true;
                break;
            }
        }
        if(!wasAdded && score.score() < scoreList[index][9].score()){
            scoreList[index][9] = score;
        }
        Arrays.sort(scoreList[index], Comparator.nullsLast(Comparator.naturalOrder()));
    }
    public void saveToFile(){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("highscores"))) {
            out.writeObject(this);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static ScoreList loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("highscores"))) {
            return (ScoreList) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            // If file not found or error occurs, return a new scoreboard.
            return new ScoreList();
        }
    }
}
