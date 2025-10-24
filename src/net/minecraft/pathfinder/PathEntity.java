
package net.minecraft.pathfinder;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3D;

public class PathEntity {

    private final PathPoint[] points;
    public final int pathLength;
    private int pathIndex;

    public PathEntity(final PathPoint[] arr) {
        this.points = arr;
        this.pathLength = arr.length;
    }

    public void incrementPathIndex() {
        ++this.pathIndex;
    }

    public boolean isFinished() {
        return this.pathIndex >= this.points.length;
    }

    public Vec3D getPosition(final Entity eq) {
        return Vec3D.createVector(this.points[this.pathIndex].xCoord + (int) (eq.width + 1.0f) * 0.5f, (float) this.points[this.pathIndex].yCoord, this.points[this.pathIndex].zCoord + (int) (eq.width + 1.0f) * 0.5f);
    }
}
