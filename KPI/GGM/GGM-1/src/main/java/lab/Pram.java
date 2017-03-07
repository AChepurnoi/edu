package lab; /**
 * Created by Sasha on 3/7/17.
 */

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sasha on 3/6/17.
 */
public class Pram {

    private int OFFSET_X = 0;
    private int OFFSET_Y = 0;

    public Pram() {
    }

    public Pram(int OFFSET_X, int OFFSET_Y) {
        this.OFFSET_X = OFFSET_X;
        this.OFFSET_Y = OFFSET_Y;
    }

    private List<Shape> wheels(){

        Line line = new Line(OFFSET_X + 40,OFFSET_Y + 150,OFFSET_X + 150,OFFSET_Y + 120);
        Line line2 = new Line(OFFSET_X + 160,OFFSET_Y + 150,OFFSET_X + 30,OFFSET_Y + 120);

        Circle wheel = new Circle(25);
        wheel.setCenterX(OFFSET_X + 35);
        wheel.setCenterY(OFFSET_Y + 165);
        wheel.setFill(Color.BLUEVIOLET);

        Circle wheel2 = new Circle(25);
        wheel2.setCenterX(OFFSET_X + 165);
        wheel2.setCenterY(OFFSET_Y + 165);
        wheel2.setFill(Color.BLUEVIOLET);

        return Arrays.asList(line,line2,wheel,wheel2);
    }

    public List<Shape> drawableParts(){
        List<Shape> shapes = new ArrayList<Shape>();
        shapes.addAll(wheels());
        shapes.addAll(handlebar());
        shapes.addAll(body());
        return shapes;

    }

    private List<Shape> body(){
        Shape polygon = new Polygon(OFFSET_X, OFFSET_Y,
                OFFSET_X, OFFSET_Y + 120,
                OFFSET_X + 200, OFFSET_Y + 120,
                OFFSET_X + 200, OFFSET_Y + 50,
                OFFSET_X + 50, OFFSET_Y + 50,
                OFFSET_X, OFFSET_Y);
        polygon.setFill(Color.AQUA);

        return Arrays.asList(polygon);
    }

    private List<Shape> handlebar(){
        Shape line3 = new Line(OFFSET_X + 200,OFFSET_Y + 50,OFFSET_X + 230,OFFSET_Y + 25);
        return Arrays.asList(line3);
    }

}
