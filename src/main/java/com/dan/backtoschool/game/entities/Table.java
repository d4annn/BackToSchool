package com.dan.backtoschool.game.entities;

import com.dan.backtoschool.engine.BoundingBox;
import com.dan.backtoschool.engine.scene.Entity;
import org.joml.Vector3f;

import java.util.Random;

public class Table extends Entity {

    private boolean moving;
    private int ticksRemaining;
    private BoundingBox boundingBox;

    public Table(String id, String modelId) {
        super(id, modelId);
        boundingBox = new BoundingBox(new Vector3f(getPosition()).add(-.8f, -5, -.8f), new Vector3f(getPosition()).add(-2f, 5f, -2f));
        moving = false;
        ticksRemaining = 0;
    }

    public void onCollision() {
        moving = true;
        ticksRemaining = 5000;
    }

    public boolean collidesWithEntity(Vector3f position) {
        return boundingBox.collides(position);
    }


    public void update(Room room) {
        if (moving) {
            move(new Vector3f(0.05f, 0, 0.05f), new Vector3f(), room);
            ticksRemaining--;
            if (ticksRemaining == 0) {
                moving = false;
            }
            updateModelMatrix();
        }
    }

    public void move(Vector3f vector3f, Vector3f rotation, Room room) {
        Vector3f newPos = new Vector3f(getPosition()).add(vector3f);
        if (room.collidesWithFloor(newPos)) {
            if (getPosition().y < room.getLowestY()) {
                vector3f.y = room.getLowestY();
            } else
                vector3f.y = 0;
        }
        getPosition().add(vector3f);
    }

    @Override
    public void setPosition(float x, float y, float z) {
        super.setPosition(x, y, z);
        boundingBox = new BoundingBox(new Vector3f(getPosition()).add(-.5f, -5, -.5f), new Vector3f(getPosition()).add(0.4f, 5f, 0.4f));
    }
}
