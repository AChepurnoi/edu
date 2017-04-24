package lab;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.LinkedList;

abstract class FigureBuilder{

    protected ArrayList<Shape> figureParts;

    public FigureBuilder() {

    }

    abstract public Group build();

    protected void addSingleShape(ShapeCreator shapeCreator, Color shapeBaseColor, Double[] shapePoints) {

        this.figureParts.add(shapeCreator.create(shapeBaseColor, shapePoints));
    }

    protected void addSingleShape(ShapeCreator shapeCreator, Color shapeBaseColor,
                                   Double[] shapePoints, Color borderColor, double borderWidth) {
        this.figureParts.add(shapeCreator.create(shapeBaseColor, shapePoints, borderColor, borderWidth));
    }

    protected void addShapesPair(ShapeCreator shapeCreator, Color shapeBaseColor,
                                    Double[] firstShapePoints, Double[] secondShapePoints) {

        this.figureParts.add(shapeCreator.create(shapeBaseColor, firstShapePoints));
        this.figureParts.add(shapeCreator.create(shapeBaseColor, secondShapePoints));
    }

    protected void addShapesPair(ShapeCreator shapeCreator, Color shapeBaseColor,
                                    Double[] firstShapePoints, Double[] secondShapePoints,
                                    Color shapeBorderColor, double borderWidth) {
        this.figureParts.add(shapeCreator.create(shapeBaseColor, firstShapePoints, shapeBorderColor, borderWidth));
        this.figureParts.add(shapeCreator.create(shapeBaseColor, secondShapePoints, shapeBorderColor, borderWidth));
    }
}