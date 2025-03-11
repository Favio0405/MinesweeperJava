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

public class TopButton extends JButton {
    private static final Map<String, ImageIcon[]> sprites = new HashMap<>();
    private String currentIconKey;
    private boolean pressed;

    static{
        loadSprites();
    }

    public TopButton(){
        super();
        currentIconKey = "Normal";
        pressed = false;
        setFocusable(false);
        setModel(new FixedButtonModel());
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e){
                scaleIcon();
            }
        });
        setPreferredSize(new Dimension(50, 50));
    }

    public void setNormalIcon(){
        currentIconKey = "Normal";
        scaleIcon();
    }
    public void setOpenIcon(){
        currentIconKey = "Open";
        scaleIcon();
    }
    public void setWinnerIcon(){
        currentIconKey = "Winner";
        scaleIcon();
    }
    public void setLoserIcon(){
        currentIconKey = "Loser";
        scaleIcon();
    }
    public void togglePressedIcon(){
        pressed = !pressed;
        scaleIcon();
    }
    private void scaleIcon() {
        int width = getWidth();
        int height = getHeight();
        if (width > 0 && height > 0) {
            int i = pressed ? 1 : 0;
            ImageIcon icon = sprites.get(currentIconKey)[i];
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(scaledImage));
        }
    }
    private static void loadSprites(){
        Path spriteDirectory = Paths.get("sprites/TopButton");
        try(Stream<Path> paths = Files.walk(spriteDirectory)){
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            BufferedImage image = ImageIO.read(path.toFile());
                            if(image != null){
                                String[] strs = path.getFileName().toString().split(" ");
                                strs[1] = strs[1].replaceAll("\\.png$", "");
                                if(!sprites.containsKey(strs[0]))
                                    sprites.put(strs[0], new ImageIcon[2]);

                                if(strs[1].equalsIgnoreCase("unpressed"))
                                    sprites.get(strs[0])[0] = new ImageIcon(image);
                                else if(strs[1].equalsIgnoreCase("pressed"))
                                    sprites.get(strs[0])[1] = new ImageIcon(image);
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
