import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class GridButton extends JButton{
    /*
    Numbers represent the amount of mines around the square. Mines are represented with -1
     */
    public final int row;
    public final int column;
    public int number;
    public boolean isCovered;
    private boolean isFlagged;
    private final static Map<String, ImageIcon> sprites = new HashMap<>();
    static{
        loadSprites();
    }
    private ImageIcon currentIcon;
    public GridButton(int number, int row, int column){
        super();
        this.number = number;
        this.row = row;
        this.column = column;
        this.isCovered = true;
        this.isFlagged = false;
        setFocusable(false);
        setModel(new FixedButtonModel());
        currentIcon = sprites.get("CoveredCell");
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e){
                scaleIcon();
            }
        });
    }

    private void scaleIcon() {
        if (currentIcon != null) {
            int width = getWidth();
            int height = getHeight();
            if (width > 0 && height > 0) {
                Image scaledImage = currentIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(scaledImage));
            }
        }
    }

    public void uncover(){
        if(isFlagged || !isCovered) return;
        this.isCovered = false;
        if(number == -1){
            currentIcon = sprites.get("ActivatedMine");
        }
        else if(number == 0){
            currentIcon = sprites.get("EmptyCell");
        }
        else{
            currentIcon = sprites.get(String.valueOf(number));
        }
        scaleIcon();
    }
    public void toggleFlag(){
        if(!isCovered) return;
        isFlagged = !isFlagged;
        currentIcon = (isFlagged) ? sprites.get("Flag") : sprites.get("CoveredCell");
        scaleIcon();
    }
    public boolean isFlagged() {
        return isFlagged;
    }
    public void pressedButton(){
        if(!isCovered || isFlagged) return;
        currentIcon = sprites.get("EmptyCell");
        scaleIcon();
    }
    public void unpressedButton(){
        if(!isCovered || isFlagged) return;
        currentIcon = sprites.get("CoveredCell");
        scaleIcon();
    }
    private static void loadSprites(){
        Path spriteDirectory = Paths.get("sprites/GridButton");
        try(Stream<Path> paths = Files.walk(spriteDirectory)){
            paths.filter(Files::isRegularFile)
                 .forEach(path -> {
                    try {
                        BufferedImage image = ImageIO.read(path.toFile());
                        if(image != null){
                            sprites.put(path.getFileName().toString()
                                    .replaceAll("\\.png$", ""), new ImageIcon(image));
                        }
                    } catch (IOException e) {
                        System.err.println("Error loading sprite");
                        e.printStackTrace();
                    }
            });
        }catch (IOException e){
            System.err.println("Error reading the sprites directory.");
            e.printStackTrace();
        }
    }
}
