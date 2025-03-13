public interface MenuListener{
    void createGame(int[] boardSize, int mines, String difficulty);
    void invalidSelection(String validSelection);
}
