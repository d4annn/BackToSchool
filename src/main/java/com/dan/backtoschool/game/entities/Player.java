package com.dan.backtoschool.game.entities;

import com.dan.backtoschool.engine.scene.Camera;
import com.dan.backtoschool.engine.scene.Entity;
import com.dan.backtoschool.engine.util.Pair;
import org.joml.Vector3f;

public class Player extends Entity {

    private Camera camera;
    private Vector3f direction;
    private Vector3f right;
    private Vector3f up;
    private int ticksJumping = 0;
    private Vector3f speed;

    public Player(String id, String modelId) {
        super(id, modelId);
        direction = new Vector3f();
        right = new Vector3f();
        up = new Vector3f();
        speed = new Vector3f();
    }

    public void jump(Room room) {
        if (room.collidesWithFloor(new Vector3f(getPosition()).add(0, -0.02f, 0))) {
            ticksJumping = 8;
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void update(Room room, double deltaTime) {
        if (ticksJumping > 0) {
            ticksJumping--;
            speed.add(0, 0.1f, 0);
        }
        speed.add(0, -0.02f, 0);
        move(new Vector3f(speed), room);
    }

    public void move(Vector3f movement, Room room) {
        Vector3f newPos = new Vector3f(this.getPosition()).add(movement);
        //check collision
        if (room.collidesWithFloor(newPos)) {
            if (getPosition().y < room.getLowestY()) {
                movement.y = room.getLowestY();
            } else
                movement.y = 0;
        }
        this.getPosition().add(movement);
        camera.getPosition().add(movement);
        speed = new Vector3f();
    }

    public void checkObjectCollision(Room room, Vector3f newPos, Vector3f direction) {
        Pair<Integer, Boolean> pair = room.collidesWithWall(newPos);
        if (pair.getRight()) {
            direction.x = 0;
            direction.z = 0;
        }
    }

    public void moveBackwards(float inc, Room room) {
        camera.getViewMatrix().positiveZ(direction);
        direction.y = 0;
        direction.normalize();
        direction.mul(inc);
        Vector3f vec = new Vector3f(getPosition()).add(direction);
        checkObjectCollision(room, vec, direction);
        getPosition().add(direction);
        camera.getPosition().add(direction);
        camera.recalculate();
    }

    public void moveDown(float inc) {
        camera.getViewMatrix().positiveY(up).mul(inc);
        getPosition().sub(up);

        camera.getPosition().sub(up);
        camera.recalculate();
    }

    public void moveForward(float inc, Room room) {
        camera.getViewMatrix().positiveZ(direction);
        direction.y = 0;
        direction.normalize();
        direction.negate();
        direction.mul(inc);
        Vector3f vec = new Vector3f(getPosition()).add(direction);
        checkObjectCollision(room, vec, direction);
        getPosition().add(direction);
        camera.getPosition().add(direction);
        camera.recalculate();
    }

    public void moveLeft(float inc, Room room) {
        camera.getViewMatrix().positiveX(right).mul(inc);
        Vector3f vec = new Vector3f(getPosition()).add(right);
        checkObjectCollision(room, vec, right);
        getPosition().sub(right);
        camera.getPosition().sub(right);
        camera.recalculate();
    }

    public void moveRight(float inc, Room room) {
        camera.getViewMatrix().positiveX(right).mul(inc);
        Vector3f vec = new Vector3f(getPosition()).add(right);
        checkObjectCollision(room, vec, right);
        getPosition().add(right);
        camera.getPosition().add(right);
        camera.recalculate();
    }

    public void moveUp(float inc) {
        camera.getViewMatrix().positiveY(up).mul(inc);
        getPosition().add(up);
        camera.getPosition().add(up);
        camera.recalculate();
    }


}

