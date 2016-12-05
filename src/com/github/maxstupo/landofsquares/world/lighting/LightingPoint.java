package com.github.maxstupo.landofsquares.world.lighting;

import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.landofsquares.world.World;

/**
 * @author Maxstupo
 *
 */
public class LightingPoint {

    private final World world;

    private final int x;
    private final int y;
    private final int lightValue;

    private final boolean isSource;

    public LightingPoint(World world, int x, int y, int lightValue, boolean isSource) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.lightValue = lightValue;
        this.isSource = isSource;
    }

    public List<LightingPoint> getNeighbors() {
        if (world.getBlock(x, y).getLightBlocking() < 0)
            return new ArrayList<>();

        int newValue = lightValue - 1 - world.getBlock(x, y).getLightBlocking();
        return getExactNeighbors(newValue);
    }

    public List<LightingPoint> getExactNeighbors(int lightingValue) {
        List<LightingPoint> neighbors = new ArrayList<>();

        boolean okayLeft = x > 0;
        boolean okayRight = x < world.getWidth() - 1;
        boolean okayUp = y > 0;
        boolean okayDown = y < world.getHeight() - 1;

        if (okayRight) {
            neighbors.add(new LightingPoint(world, x + 1, y, lightingValue, false));

            if (okayUp)
                neighbors.add(new LightingPoint(world, x + 1, y - 1, lightingValue, false));

            if (okayDown)
                neighbors.add(new LightingPoint(world, x + 1, y + 1, lightingValue, false));
        }

        if (okayLeft) {
            neighbors.add(new LightingPoint(world, x - 1, y, lightingValue, false));

            if (okayUp)
                neighbors.add(new LightingPoint(world, x - 1, y - 1, lightingValue, false));

            if (okayDown)
                neighbors.add(new LightingPoint(world, x - 1, y + 1, lightingValue, false));
        }

        if (okayDown)
            neighbors.add(new LightingPoint(world, x, y + 1, lightingValue, false));

        if (okayUp)
            neighbors.add(new LightingPoint(world, x, y - 1, lightingValue, false));

        return neighbors;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isSource ? 1231 : 1237);
        result = prime * result + lightValue;
        result = prime * result + ((world == null) ? 0 : world.hashCode());
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LightingPoint other = (LightingPoint) obj;
        if (isSource != other.isSource)
            return false;
        if (lightValue != other.lightValue)
            return false;
        if (world == null) {
            if (other.world != null)
                return false;
        } else if (!world.equals(other.world))
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLightValue() {
        return lightValue;
    }

    public boolean isSource() {
        return isSource;
    }

}
