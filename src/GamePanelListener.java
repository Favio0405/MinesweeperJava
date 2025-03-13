public interface GamePanelListener {
    void reset(int rows, int columns, int mines, String difficulty);
    void addScore(Score score);
}
