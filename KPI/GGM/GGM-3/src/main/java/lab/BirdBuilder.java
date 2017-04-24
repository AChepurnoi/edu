package lab;
import javafx.scene.Group;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class BirdBuilder extends FigureBuilder{

    public BirdBuilder() {
        super();
    }

    private ShapeCreator arcCreator;
    private ShapeCreator circleCreator;
    private ShapeCreator polygonCreator;

    public void setArcCreator(ArcCreator creator) {
        this.arcCreator = creator;
    }

    public void setCircleCreator(CircleCreator creator) {
        this.circleCreator = creator;
    }

    public void setPolygonCreator(PolygonCreator creator) {
        this.polygonCreator = creator;
    }

    @Override
    public Group build() {
        this.figureParts = new ArrayList<>();
        addSingleShape(polygonCreator, ColorsProvider.FACE_BASE_COLOR, PointsProvider.FACE_POINTS,
                ColorsProvider.FACE_BORDER_COLOR, PointsProvider.FACE_BORDER_SIZE);

        addSingleShape(arcCreator, ColorsProvider.CHIN_COLOR, PointsProvider.CHIN_POINTS);
        addShapesPair(polygonCreator, ColorsProvider.TAIL_COLOR, PointsProvider.TOP_TAIL_POINTS, PointsProvider.BOTTOM_TAIL_POINTS);

        addShapesPair(polygonCreator, ColorsProvider.BEAK_BASE_COLOR, PointsProvider.LEFT_BEAK_POINTS,
                PointsProvider.RIGHT_BEAK_POINTS, ColorsProvider.BEAK_BORDER_COLOR, PointsProvider.BEAK_BORDER_SIZE);

        addShapesPair(polygonCreator, ColorsProvider.EYE_BASE_COLOR, PointsProvider.LEFT_EYE_POINTS,
                PointsProvider.RIGHT_EYE_POINTS, ColorsProvider.EYE_BORDER_COLOR, PointsProvider.EYE_BORDER_SIZE);

        addShapesPair(circleCreator, ColorsProvider.EYE_PUPIL_BASE_COLOR, PointsProvider.LEFT_EYE_PUPIL_POINTS,
                PointsProvider.RIGHT_EYE_PUPIL_POINTS);

        addShapesPair(polygonCreator, ColorsProvider.EYEBROW_COLOR, PointsProvider.LEFT_EYEBROW_POINTS,
                PointsProvider.RIGHT_EYEBROW_POINTS);

        Group group = new Group();
        for(Shape shape : figureParts){
            group.getChildren().add(shape);
        }

        return group;
    }
}
