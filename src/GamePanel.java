import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.Random;
import javax.swing.Timer;


public class GamePanel extends JPanel{
    /*
    Numbers represent the amount of mines around the square. Mines are represented with -1
     */
    private final JPanel gridPanel;
    private final JPanel topMenu;
    private final TopButton topButton;
    private final SegmentDisplay mineCounter;
    private final SegmentDisplay counter;
    private final Timer timer;
    private final GridButton[][] board;
    private final GridButtonListener gridButtonListener;
    private GamePanelListener panelListener;
    private boolean gameOver;
    private final int ROWS;
    private final int COLUMNS;
    private final int MINES;
    private final String DIFFICULTY;
    private final int NUMBER_OF_NUMBERED_SQUARES;
    private int uncoveredCount;
    private int unflaggedMines;
    private int time;

    public GamePanel(int rows, int columns, int mines, String DIFFICULTY){
        super();
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.MINES = mines;
        this.DIFFICULTY = DIFFICULTY;
        this.NUMBER_OF_NUMBERED_SQUARES = (ROWS * COLUMNS) - MINES;
        this.gameOver = false;
        this.unflaggedMines = MINES;
        this.board = new GridButton[rows][columns];

        gridPanel = new JPanel();
        generateBoard();
        gridPanel.setLayout(new GridLayout(rows, columns));
        gridButtonListener = new GridButtonListener();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                board[i][j].addMouseListener(gridButtonListener);
                gridPanel.add(board[i][j]);
            }
        }
        gridPanel.setPreferredSize(new Dimension(COLUMNS * 30, ROWS * 30));

        setLayout(new BorderLayout());

        FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 0 , 0);
        topMenu = new JPanel(flow);

        mineCounter = new SegmentDisplay(3);
        mineCounter.setNumber(mines);
        topMenu.add(mineCounter);

        topButton = new TopButton();
        topButton.addMouseListener(new TopButtonListener());
        JPanel topButtonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topButtonWrapper.add(topButton);
        topMenu.add(topButtonWrapper);

        counter = new SegmentDisplay(3);
        counter.setNumber(0);
        timer = new Timer(1000, e -> {
            if(time >= 999) counter.setNumber(999);
            else {
                time++;
                counter.setNumber(time);
            }
        });
        topMenu.add(counter);
        topMenu.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = topMenu.getWidth();
                int componentsWidth = counter.getPreferredSize().width + mineCounter.getPreferredSize().width +
                                topButtonWrapper.getPreferredSize().width;
                int gap = Math.max(0, (panelWidth - componentsWidth) / 3);
                flow.setHgap(gap);
                topMenu.revalidate();
                topMenu.repaint();
            }
        });
        add(topMenu, BorderLayout.NORTH);
        add(gridPanel);
        timer.start();
    }
    private void generateBoard(){
        generateMines();
        generateNumbers();
    }
    private void generateMines(){
        Random rnd = new Random();
        for(int i = 0; i < MINES; i++){
            int mineRow = rnd.nextInt(ROWS);
            int mineColumn = rnd.nextInt(COLUMNS);
            if(!isMine(mineRow, mineColumn))
                board[mineRow][mineColumn] = new GridButton(-1, mineRow, mineColumn);
            else i--;
        }
    }
    private void generateNumbers(){
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                if(!isMine(i, j)) board[i][j] = new GridButton(checkSurroundingMines(i, j), i, j);
            }
        }
    }
    private int checkSurroundingMines(int row, int column){
        int mines = 0;
        if(row + 1 < ROWS && column + 1 < COLUMNS && isMine(row + 1, column + 1)){
            mines++;
        }
        if(row + 1 < ROWS && isMine(row + 1,column)){
            mines++;
        }
        if(row + 1 < ROWS && column - 1 >= 0 && isMine(row + 1, column - 1)){
            mines++;
        }
        if(column - 1 >= 0 && isMine(row, column - 1)){
            mines++;
        }
        if(row - 1 >= 0 && column - 1 >= 0 && isMine(row - 1, column - 1)){
            mines++;
        }
        if(row - 1 >= 0 && isMine(row - 1, column)){
            mines++;
        }
        if(row - 1 >= 0 && column + 1 < COLUMNS && isMine(row - 1, column + 1)){
            mines++;
        }
        if(column + 1 < COLUMNS && isMine(row,column + 1)){
            mines++;
        }
        return mines;
    }
    private int checkSurroundingFlags(GridButton button){
        int row = button.row;
        int column = button.column;
        int flags = 0;
        if(row + 1 < ROWS && column + 1 < COLUMNS && board[row + 1][column + 1].isFlagged()){
            flags++;
        }
        if(row + 1 < ROWS && board[row + 1][column].isFlagged()){
            flags++;
        }
        if(row + 1 < ROWS && column - 1 >= 0 && board[row + 1][column - 1].isFlagged()){
            flags++;
        }
        if(column - 1 >= 0 && board[row][column - 1].isFlagged()){
            flags++;
        }
        if(row - 1 >= 0 && column - 1 >= 0 && board[row - 1][column - 1].isFlagged()){
            flags++;
        }
        if(row - 1 >= 0 && board[row - 1][column].isFlagged()){
            flags++;
        }
        if(row - 1 >= 0 && column + 1 < COLUMNS && board[row - 1][column + 1].isFlagged()){
            flags++;
        }
        if(column + 1 < COLUMNS && board[row][column + 1].isFlagged()){
            flags++;
        }
        return flags;
    }
    private boolean isMine(int row, int column){
        return board[row][column] != null && board[row][column].number == -1;
    }
    private void activateButton(GridButton button){
        if(button.isFlagged() || !button.isCovered) return;
        else if(button.number == -1){
            button.uncover();
            gameOverLoss();
        }
        else if(button.number == 0){
            revealEmptyCell(button);
        }
        else{
            button.uncover();
            uncoveredCount++;
        }
        if(uncoveredCount == NUMBER_OF_NUMBERED_SQUARES){
            gameOverWin();
        }
    }

    private void activateNumber(GridButton button) {
        if(button.number != checkSurroundingFlags(button)) return;
        int row = button.row;
        int column = button.column;
        if(row + 1 < ROWS && column + 1 < COLUMNS){
            activateButton(board[row + 1][column + 1]);
        }
        if(row + 1 < ROWS){
            activateButton(board[row + 1][column]);
        }
        if(row + 1 < ROWS && column - 1 >= 0){
            activateButton(board[row + 1][column - 1]);
        }
        if(column - 1 >= 0){
            activateButton(board[row][column - 1]);
        }
        if(row - 1 >= 0 && column - 1 >= 0){
            activateButton(board[row -1][column - 1]);
        }
        if(row - 1 >= 0){
            activateButton(board[row - 1][column]);
        }
        if(row - 1 >= 0 && column + 1 < COLUMNS){
            activateButton(board[row - 1][column + 1]);
        }
        if(column + 1 < COLUMNS){
            activateButton(board[row][column + 1]);
        }
    }

    private void revealEmptyCell(GridButton button){
        boolean[][] visited = new boolean[ROWS][COLUMNS];
        revealEmptyCellsRecursive(button, visited);
    }
    private void revealEmptyCellsRecursive(GridButton button, boolean[][] visited){
        int row = button.row;
        int column = button.column;
        if(visited[row][column]) return;
        else visited[row][column] = true;
        if(isMine(row, column)) return;
        if(button.isCovered && !button.isFlagged()){
            button.uncover();
            uncoveredCount++;
        }
        if(button.number > 0){
            return;
        }
        if(row + 1 < ROWS && column + 1 < COLUMNS){
            revealEmptyCellsRecursive(board[row + 1][column + 1], visited);
        }
        if(row + 1 < ROWS){
            revealEmptyCellsRecursive(board[row + 1][column], visited);
        }
        if(row + 1 < ROWS && column - 1 >= 0){
            revealEmptyCellsRecursive(board[row + 1][column - 1], visited);
        }
        if(column - 1 >= 0){
            revealEmptyCellsRecursive(board[row][column - 1], visited);
        }
        if(row - 1 >= 0 && column - 1 >= 0){
            revealEmptyCellsRecursive(board[row -1][column - 1], visited);
        }
        if(row - 1 >= 0){
            revealEmptyCellsRecursive(board[row - 1][column], visited);
        }
        if(row - 1 >= 0 && column + 1 < COLUMNS){
            revealEmptyCellsRecursive(board[row - 1][column + 1], visited);
        }
        if(column + 1 < COLUMNS){
            revealEmptyCellsRecursive(board[row][column + 1], visited);
        }
    }
    private void gameOverLoss(){
        timer.stop();
        removeGridButtonListeners();
        topButton.setLoserIcon();
        gameOver = true;
    }
    private void gameOverWin(){
        timer.stop();
        removeGridButtonListeners();
        topButton.setWinnerIcon();
        gameOver = true;
        panelListener.addScore(new Score(time, LocalDateTime.now(), DIFFICULTY));
    }

    private void removeGridButtonListeners(){
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLUMNS; j++){
                board[i][j].removeMouseListener(gridButtonListener);
            }
        }
    }

    public void setListener(GamePanelListener panelListener){
        this.panelListener = panelListener;
    }

    private class GridButtonListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            GridButton button = (GridButton) e.getSource();
            if(SwingUtilities.isLeftMouseButton(e)){
                button.pressedButton();
                topButton.setOpenIcon();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point point = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), gridPanel);
            Component comp = SwingUtilities.getDeepestComponentAt(gridPanel, point.x, point.y);
            if(comp instanceof GridButton button) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if(!button.isCovered && button.number > 0) activateNumber(button);
                    else activateButton(button);
                } else if (SwingUtilities.isRightMouseButton(e) && button.isCovered) {
                    if(button.isFlagged()) unflaggedMines++;
                    else unflaggedMines--;
                    if(unflaggedMines > 999) mineCounter.setNumber(999);
                    else if(unflaggedMines <= -99) mineCounter.setNumber(-99);
                    else {
                        mineCounter.setNumber(unflaggedMines);
                        button.toggleFlag();
                    }
                }
            }
            if(!gameOver) topButton.setNormalIcon();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            GridButton button = (GridButton) e.getSource();
            if((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0){
                button.pressedButton();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            GridButton button = (GridButton) e.getSource();
            if(button.isCovered){
                button.unpressedButton();
            }
        }
    }

    private class TopButtonListener extends MouseAdapter{
        public void mousePressed(MouseEvent e) {
            TopButton button = (TopButton) e.getSource();
            if(SwingUtilities.isLeftMouseButton(e)){
                button.togglePressedIcon();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            TopButton topButton = (TopButton) e.getSource();
            topButton.togglePressedIcon();
            Point point = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), topMenu);
            Component comp = SwingUtilities.getDeepestComponentAt(topMenu, point.x, point.y);
            if(comp instanceof TopButton){
                if(SwingUtilities.isLeftMouseButton(e)) {
                    panelListener.reset(ROWS, COLUMNS, MINES, DIFFICULTY);
                }
            }
        }
    }
}
