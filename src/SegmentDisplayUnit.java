import javax.swing.*;
import java.awt.*;

public class SegmentDisplayUnit extends JPanel {
    private int digit;
    public SegmentDisplayUnit(){
        super(new BorderLayout());
        digit = -2;
        setBackground(Color.black);
    }
    public void setDigit(int digit) {
        this.digit = digit;
        repaint();
    }

    public int getDigit() {
        return digit;
    }

    private boolean[] segmentsForDigit(int digit) {
        return switch (digit) {
            case -1 -> new boolean[]{false, false, false, false, false, false, true}; // just negative sign not -1.
            case 0 -> new boolean[]{true, true, true, true, true, true, false};
            case 1 -> new boolean[]{false, true, true, false, false, false, false};
            case 2 -> new boolean[]{true, true, false, true, true, false, true};
            case 3 -> new boolean[]{true, true, true, true, false, false, true};
            case 4 -> new boolean[]{false, true, true, false, false, true, true};
            case 5 -> new boolean[]{true, false, true, true, false, true, true};
            case 6 -> new boolean[]{true, false, true, true, true, true, true};
            case 7 -> new boolean[]{true, true, true, false, false, false, false};
            case 8 -> new boolean[]{true, true, true, true, true, true, true};
            case 9 -> new boolean[]{true, true, true, true, false, true, true};
            default -> new boolean[]{false, false, false, false, false, false, false};
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double baseWidth = 35.0;
        double baseHeight = 50.0;

        double scaleX = getWidth() / baseWidth;
        double scaleY = getHeight() / baseHeight;

        double scale = Math.min(scaleX, scaleY);
        g2.scale(scale, scale);

        boolean[] segs = segmentsForDigit(digit);
        Color onColor = Color.RED;
        Color offColor = Color.DARK_GRAY;

        int thickness = 6;
        int horizontalLength = 24;
        int verticalHeight = 20;
        int xOffset = (35 - horizontalLength) / 2;

        // Segment A (top horizontal) - touches top edge
        g2.setColor(segs[0] ? onColor : offColor);
        g2.fillRect(xOffset, 0, horizontalLength, thickness);

        // Segment B (upper right vertical) - touches right edge
        g2.setColor(segs[1] ? onColor : offColor);
        g2.fillRect(29, 0, thickness, verticalHeight);

        // Segment C (lower right vertical) - touches right edge
        g2.setColor(segs[2] ? onColor : offColor);
        g2.fillRect(29, verticalHeight + thickness, thickness, verticalHeight);

        // Segment D (bottom horizontal) - touches bottom edge
        g2.setColor(segs[3] ? onColor : offColor);
        g2.fillRect(xOffset, (int)(baseHeight - thickness), horizontalLength, thickness);

        // Segment E (lower left vertical) - touches left edge
        g2.setColor(segs[4] ? onColor : offColor);
        g2.fillRect(0, verticalHeight + thickness, thickness, verticalHeight);

        // Segment F (upper left vertical) - touches left edge
        g2.setColor(segs[5] ? onColor : offColor);
        g2.fillRect(0, 0, thickness, verticalHeight);

        // Segment G (middle horizontal) - spans across
        g2.setColor(segs[6] ? onColor : offColor);
        g2.fillRect(xOffset, verticalHeight, horizontalLength, thickness);

        // Cleanup
        g2.dispose();
    }
}
