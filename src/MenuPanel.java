import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class MenuPanel extends JPanel {
    private final int[] boardSize = new int[2];
    private int mines;
    private final JFormattedTextField rows;
    private final JFormattedTextField columns;
    private final JFormattedTextField minesIn;
    MenuListener listener;
    public MenuPanel(){
        super();
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Select Difficulty");
        topPanel.add(label);
        JButton[] buttons = new JButton[3];
        buttons[0] = new JButton("Easy");
        buttons[0].addActionListener(e -> {
            this.boardSize[0] = 9; this.boardSize[1] = 9; mines = 10; notifyDifficultySelection("VALID");});
        topPanel.add(buttons[0]);
        buttons[1] = new JButton("Medium");
        buttons[1].addActionListener(e -> {
            this.boardSize[0] = 16; this.boardSize[1] = 16; mines = 40; notifyDifficultySelection("VALID");});
        topPanel.add(buttons[1]);
        buttons[2] = new JButton("Hard");
        buttons[2].addActionListener(e -> {
            this.boardSize[0] = 30; this.boardSize[1] = 16; mines = 99; notifyDifficultySelection("VALID");});
        topPanel.add(buttons[2]);
        JPanel bottomPanel = new JPanel(new FlowLayout());
        NumberFormat format = NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setAllowsInvalid(true);
        rows = new JFormattedTextField(formatter);
        rows.setColumns(2);
        columns = new JFormattedTextField(formatter);
        columns.setColumns(2);
        minesIn = new JFormattedTextField(formatter);
        minesIn.setColumns(3);
        JLabel rowLabel = new JLabel("Rows");
        JLabel columnLabel = new JLabel("Columns");
        JLabel minesLabel = new JLabel("Mines");
        JButton custom = new JButton("Custom");
        custom.addActionListener(e -> {
            int inputRows = (rows.getValue() == null) ? 0 : (Integer) rows.getValue();
            int inputColumns = (columns.getValue() == null) ? 0 : (Integer) columns.getValue();
            int inputMines = (minesIn.getValue() == null) ? 0 : (Integer) minesIn.getValue();
            if(inputRows <= 0 ||inputColumns <= 0 || inputMines <= 0){
                notifyDifficultySelection("RANGE");
                return;
            }
            else if(inputRows > 99 || inputColumns > 99){
                notifyDifficultySelection("RANGE");
                return;
            }
            else if(inputMines > (inputRows * inputColumns)){
                notifyDifficultySelection("MINES");
                return;
            }
            boardSize[0] = inputRows;
            boardSize[1] = inputColumns;
            mines = inputMines;
            notifyDifficultySelection("VALID");
        });
        bottomPanel.add(custom);
        bottomPanel.add(rowLabel);
        bottomPanel.add(rows);
        bottomPanel.add(columnLabel);
        bottomPanel.add(columns);
        bottomPanel.add(minesLabel);
        bottomPanel.add(minesIn);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(topPanel);
        add(bottomPanel);
    }
    public void setListener(MenuListener listener){
        this.listener = listener;
    }
    private void notifyDifficultySelection(String validSelection){
        if(listener != null && validSelection.equals("VALID")){
            listener.createGame(boardSize, mines);
        }
        else if(listener != null){
            listener.invalidSelection(validSelection);
        }
    }
}
