package lab2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by Sasha on 3/7/17.
 */
public class Pram extends JComponent {


    private double scale = 1;
    private double delta = 0.01;
    private final double R = 50;
    private double alpha = 1;
    private int CENTER_X = 50;
    private int CENTER_Y = 50;


    private static int maxWidth = 300;
    private static int maxHeight = 250;

    public Pram() {
        Timer timer = new Timer(10, event -> animationTick());
        timer.start();
    }


//    x = r*cos(a) + u; y = r*sin(a) + v,
    private double getXPosRotated(){
        return R * Math.cos(alpha) + CENTER_X;
    }

    private double getYPosRotated(){
        return R * Math.sin(alpha) + CENTER_Y;
    }

    private void animationTick() {
        if (scale < 0.01) {
            delta = -delta;
        } else if (scale > 0.99) {
            delta = -delta;
        }
        alpha = alpha + 0.05;
        if(alpha >= 360) alpha = 0;

        scale += delta;
        repaint();
    }

    public void drawDecorator(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(0, 0);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setColor(Color.CYAN);
        BasicStroke bs1 = new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(bs1);
        g2d.drawRect(10, 10, 480, 455);
        BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(bs);

    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.translate(0, 0);
        drawDecorator(g2d);
        g2d.translate(120, 100);
        animate(g2d);
        drawWheels(g2d);
        drawHandlebars(g2d);
        drawBody(g2d);

    }

    private void drawHandlebars(Graphics2D g2d) {
        g2d.drawLine(200, 50, 250, 10);
    }

    private void animate(Graphics2D g2d) {
        g2d.scale(scale, 0.99);
        g2d.translate(getXPosRotated(), getYPosRotated());

    }

    private void drawBody(Graphics2D g2d) {
        double points[][] = {
                {0, 0},
                {0, 120},
                {200, 120},
                {200, 50},
                {50, 50},
                {0, 0}
        };
        GradientPaint gp = new GradientPaint(5, 90, Color.RED, 20, 45, Color.GREEN, true);
        g2d.setPaint(gp);
        GeneralPath star = new GeneralPath();
        star.moveTo(points[0][0], points[0][1]);
        for (int k = 1; k < points.length; k++)
            star.lineTo(points[k][0], points[k][1]);
        star.closePath();
        g2d.fill(star);
    }

    private void drawWheels(Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillOval(10, 145, 50, 50);
        g2d.fillOval(145, 145, 50, 50);
        g2d.drawLine(30, 150, 200, 100);
        g2d.drawLine(175, 150, 25, 100);

    }


}
