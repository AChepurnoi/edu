package lab;

import javafx.scene.shape.Polygon;

public class PolygonCreator extends ShapeCreator {

    @Override
    protected Polygon fillShapeByPoints(Double[] points) {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(points);
        return polygon;
    }


}
