package lab;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public abstract class ShapeCreator {

    protected void setBorders(Shape polygon, Color borderColor, double borderWidth) {
        polygon.setStroke(borderColor);
        polygon.setStrokeWidth(borderWidth);
        polygon.setStrokeType(StrokeType.CENTERED);
        polygon.setStrokeLineJoin(StrokeLineJoin.ROUND);
    }

    private Shape createShapeAndFillByColor(Color baseColor, Double[] points) {
        Shape shape = fillShapeByPoints(points);
        shape.setFill(baseColor);
        return shape;
    }

    public Shape create(Color baseColor, Double[] points, Color borderColor, double borderWidth) {
        Shape shape = createShapeAndFillByColor(baseColor, points);
        setBorders(shape, borderColor, borderWidth);
        return shape;
    }


    public Shape create(Color baseColor, Double[] points) {
        Shape shape = createShapeAndFillByColor(baseColor, points);
        setBorders(shape, null, 0.0);
        return shape;
    }

    protected abstract Shape fillShapeByPoints(Double[] points);
}
