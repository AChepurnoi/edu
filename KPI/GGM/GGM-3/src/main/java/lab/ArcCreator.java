package lab;

import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

public class ArcCreator extends ShapeCreator{


    @Override
    protected Arc fillShapeByPoints(Double[] points) {
        Arc arc = new Arc(points[0], points[1], points[2], points[3], points[4], points[5]);
        arc.setType(ArcType.ROUND);
        return arc;
    }

}
