import java.io.Serializable;
import java.time.LocalDateTime;

public record Score(int score, LocalDateTime date, String difficulty) implements Serializable, Comparable<Score>{
    @Override
    public int compareTo(Score o) {
        return Integer.compare(this.score, o.score);
    }
}
