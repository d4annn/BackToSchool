package com.dan.backtoschool.engine;

import org.joml.Vector3f;

public class BoundingBox {

    private Vector3f start;
    private Vector3f end;

    public BoundingBox(Vector3f start, Vector3f end) {
        this.start = start;
        this.end = end;
    }

    public static boolean compare(BoundingBox box, Vector3f point) {
        float minX = Math.min(box.start.x, box.end.x);
        float maxX = Math.max(box.start.x, box.end.x);
        float minY = Math.min(box.start.y, box.end.y);
        float maxY = Math.max(box.start.y, box.end.y);
        float minZ = Math.min(box.start.z, box.end.z);
        float maxZ = Math.max(box.start.z, box.end.z);

        return point.x >= minX && point.x <= maxX &&
                point.y >= minY && point.y <= maxY &&
                point.z >= minZ && point.z <= maxZ;

    }

    public boolean collides(Vector3f vec) {
        return compare(this, vec);
    }

    public Vector3f getStart() {
        return start;
    }

    public void setStart(Vector3f start) {
        this.start = start;
    }

    public Vector3f getEnd() {
        return end;
    }

    public void setEnd(Vector3f end) {
        this.end = end;
    }
}
