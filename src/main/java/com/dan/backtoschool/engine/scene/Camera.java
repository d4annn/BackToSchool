package com.dan.backtoschool.engine.scene;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {


    private Vector3f position;

    private Vector2f rotation;

    private Matrix4f viewMatrix;
    private Matrix4f invViewMatrix;

    public Camera() {

        position = new Vector3f();
        viewMatrix = new Matrix4f();
        rotation = new Vector2f();
        invViewMatrix = new Matrix4f();
    }

    public Matrix4f getInvViewMatrix() {
        return invViewMatrix;
    }


    public void addRotation(float x, float y) {
        rotation.add(x, y);
        recalculate();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f vector3f) {
        setPosition(vector3f.x, vector3f.y, vector3f.z);
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public void recalculate() {
        viewMatrix.identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z);
        invViewMatrix.set(viewMatrix).invert();
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        recalculate();
    }

    public void setRotation(float x, float y) {
        rotation.set(x, y);
        recalculate();
    }
}
