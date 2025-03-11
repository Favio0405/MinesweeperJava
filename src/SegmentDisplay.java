import javax.swing.*;
import java.awt.*;

public class SegmentDisplay extends JPanel{
    private final SegmentDisplayUnit[] displayUnits;
    private final int digits;
    public SegmentDisplay(int digits){
        this.digits = digits;
        setLayout(new GridLayout(1, digits, 5, 0));
        displayUnits = new SegmentDisplayUnit[digits];
        for(int i = 0; i < digits; i++){
            displayUnits[i] = new SegmentDisplayUnit();
            displayUnits[i].setPreferredSize(new Dimension(35, 50));
            add(displayUnits[i], BorderLayout.PAGE_END);
        }
        setPreferredSize(new Dimension(digits * 35, 50));
        setBackground(Color.BLACK);
    }
    public void setNumber(int number){
        int i;
        for(i = 0; i < digits; i++){
            displayUnits[i].setDigit(-2);
        }
        int numDigits = (number == 0) ? 1 : (int) Math.log10(Math.abs(number)) + 1;
        boolean isNegative = number < 0;
        if(numDigits > digits || (isNegative && numDigits > digits - 1)) return;
        number = Math.abs(number);

        for(i = 0; i < numDigits; i++){
            int digit = number % 10;
            displayUnits[digits - i - 1].setDigit(digit);
            number /= 10;
        }
        if(isNegative){
            displayUnits[digits - i - 1].setDigit(-1);
        }
        else if(numDigits != digits){
            i = 0;
            while(displayUnits[i].getDigit() < 0){
                displayUnits[i].setDigit(0);
                i++;
            }
        }
    }
}
