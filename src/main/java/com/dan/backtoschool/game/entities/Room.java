package com.dan.backtoschool.game.entities;

import com.dan.backtoschool.engine.BoundingBox;
import com.dan.backtoschool.engine.scene.Entity;
import com.dan.backtoschool.engine.util.Pair;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Room extends Entity {

    private BoundingBox floorDimension;
    private List<BoundingBox> walls;
    private List<Table> tables;

    public Room(String id, String modelId) {
        super(id, modelId);
        setFloorDimension(new BoundingBox(new Vector3f(-250f, -150, -250f), new Vector3f(250, 1.5f, 250f)));
        walls = new ArrayList<>();
        //left door wall
        walls.add(new BoundingBox(new Vector3f(2.5f, 0, -0.1f), new Vector3f(2f, 7, -8f)));
        walls.add(new BoundingBox(new Vector3f(2.5f, 0, 0.2f), new Vector3f(2f, 7, 9f)));
        tables = new ArrayList<>();
    }

    public void addTable(Table table) {
        tables.add(table);
    }

    public List<Table> getTables() {
        return tables;
    }

    public float getLowestY() {
        return floorDimension.getEnd().y;
    }

    public List<BoundingBox> getWalls() {
        return walls;
    }

    //(-2,446E+0  3,339E-1  1,144E+0)
    //( 5,626E+0  8,455E-1 -8,773E-1)

    public BoundingBox getFloorDimension() {
        return floorDimension;
    }

    public void setFloorDimension(BoundingBox floorDimension) {
        this.floorDimension = floorDimension;
    }

    public Pair<Integer, Boolean> collidesWithWall(Vector3f position) {
        for (BoundingBox box : walls) {
            if (box.collides(position)) {
                return new Pair<Integer, Boolean>(1, true);
            }
        }
        return new Pair<Integer, Boolean>(-1, false);
    }

    public boolean collidesWithFloor(Vector3f position) {
        return floorDimension.collides(position);
    }

    public enum Parts {
        FLOOR,
        WALL,
        DOOR
    }
}
