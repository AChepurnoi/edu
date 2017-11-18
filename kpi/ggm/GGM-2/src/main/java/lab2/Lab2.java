package lab2;


import javax.swing.*;
import java.awt.*;

/**
 * Created by Sasha on 3/7/17.
 */
@SuppressWarnings("serial")
public class Lab2 extends JPanel {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Lab2");
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.add(new Pram());
        frame.setVisible(true);

    }
}
