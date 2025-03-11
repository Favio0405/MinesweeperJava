import javax.swing.*;
import java.awt.*;
//Todo: scoreboard
public class GameFrame extends JFrame implements MenuListener, GamePanelListener, BottomBarListener{
    private final MenuPanel menu;
    private GamePanel board;
    private final BottomBar bottomBar;
    private final JDialog menuDialog;
    private final JDialog errorDialog;
    private final JLabel errorLabel;

    public GameFrame(){
        super();
        setTitle("MineSweeper");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        menu = new MenuPanel();
        menu.setListener(this);
        add(menu, BorderLayout.CENTER);

        menuDialog = new JDialog(this, "Menu", true);

        errorDialog = new JDialog(this, "Error", true);
        errorDialog.setLayout(new BorderLayout());
        errorLabel = new JLabel();
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> errorDialog.setVisible(false));

        errorDialog.add(errorLabel, BorderLayout.NORTH);
        errorDialog.add(okButton, BorderLayout.SOUTH);

        bottomBar = new BottomBar();
        bottomBar.setListener(this);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void createGame(int[] boardSize, int mines) {
        if(board != null){
            remove(board);
            board = null;
        }
        board = new GamePanel(boardSize[0], boardSize[1], mines);
        board.setListener(this);
        remove(menu);
        add(board);
        add(bottomBar, BorderLayout.SOUTH);
        if(menuDialog.isVisible()) menuDialog.setVisible(false);
        pack();
        setLocationRelativeTo(null);
    }
    @Override
    public void invalidSelection(String validSelection){
        switch(validSelection){
            case "RANGE" -> errorLabel.setText(
                    "Rows and columns have to be between 0 to 99. Mines have to be more than 0");
            case "MINES" -> errorLabel.setText("There is more mines than squares");
            default -> {
                return;
            }
        }
        errorDialog.pack();
        errorDialog.setResizable(false);
        errorDialog.setLocationRelativeTo(this);
        errorDialog.setVisible(true);
    }

    @Override
    public void reset(int rows, int columns, int mines) {
        remove(board);
        board = new GamePanel(rows, columns, mines);
        board.setListener(this);
        add(board);
        revalidate();
        repaint();
    }

    @Override
    public void toMenu() {
        menuDialog.add(menu);
        menuDialog.pack();
        menuDialog.setResizable(false);
        menuDialog.setLocationRelativeTo(this);
        menuDialog.setVisible(true);
    }
}
