package lab;

import javafx.scene.shape.Circle;

public class CircleCreator extends ShapeCreator {

    @Override
    protected Circle fillShapeByPoints(Double[] points) {
        Circle circle = new Circle(points[0], points[1], points[2]);
        return circle;
    }
}
