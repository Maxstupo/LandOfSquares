package com.github.maxstupo.landofsquares.world.lighting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.github.maxstupo.landofsquares.world.World;

/**
 * @author Maxstupo
 *
 */
public class LightingEngine {

    public static final LightValueComparator LIGHT_VALUE_COMPARATOR = new LightValueComparator();

    private final World world;

    private final List<LightingPoint> sources = new LinkedList<LightingPoint>();
    private final HashSet<LightingPoint> out = new HashSet<LightingPoint>();

    private final boolean[][] sourceMap;
    private final int[][] lightLevels;

    private final boolean isSun;

    private final int lightValueSun;

    public LightingEngine(World world, boolean isSun, int lightValueSun) {
        this.world = world;
        this.isSun = isSun;
        this.lightValueSun = lightValueSun;

        this.lightLevels = new int[world.getWidth()][world.getHeight()];
        this.sourceMap = new boolean[world.getWidth()][world.getHeight()];
    }

    public void updateTile(int x, int y, int lightLevel) {
        sourceMap[x][y] = false;
        if (isSun) {
            boolean sun = true;
            for (int i = 0; i < world.getHeight(); i++) {
                if (world.getBlock(x, i).getLightBlocking() != 0)
                    sun = false;
                sourceMap[x][i] = sun;
            }

        } else if (lightLevel > 0) {
            lightLevels[x][y] = lightLevel;
            sourceMap[x][y] = true;
        }
        resetLighting(x, y);
    }

    public void removeTile(int x, int y) {
        if (!isSun && isSource(x, y)) {
            sourceMap[x][y] = false;
            resetLighting(x, y);

        } else {
            sourceMap[x][y] = false;
            if (isSun)
                spreadLighting(getSunSources(x));
            spreadLighting(new LightingPoint(world, x, y, lightLevels[x][y], false).getNeighbors());
        }
    }

    public void init() {
        List<LightingPoint> sources = new ArrayList<LightingPoint>();

        if (isSun) {
            for (int x = 0; x < world.getWidth(); x++)
                sources.addAll(getSunSources(x));
        } else {

            for (int x = 0; x < world.getWidth(); x++) {
                for (int y = 0; y < world.getHeight(); y++) {
                    int light = world.getBlock(x, y).getLightEmiting();

                    if (light > 0) {
                        sourceMap[x][y] = true;
                        lightLevels[x][y] = light;
                    } else {
                        sourceMap[x][y] = false;
                        lightLevels[x][y] = 0;
                    }

                }
            }

        }

        spreadLighting(sources);
    }

    public void resetLighting(int x, int y) {
        int xMin = Math.max(x - lightValueSun, 0);
        int xMax = Math.min(x + lightValueSun, world.getWidth() - 1);
        int yMin = Math.max(y - lightValueSun, 0);
        int yMax = Math.min(y + lightValueSun, world.getHeight() - 1);

        sources.clear();

        boolean bufferLeft = (xMin > 0);
        boolean bufferRight = (xMax < world.getWidth() - 1);
        boolean bufferTop = (yMin > 0);
        boolean bufferBottom = (yMax < world.getHeight() - 1);

        if (bufferTop) {
            if (bufferLeft) {
                sources.add(getLightingPoint(xMin - 1, yMin - 1));
                zeroLightValue(xMin - 1, yMin - 1);
            }
            if (bufferRight) {
                sources.add(getLightingPoint(xMax + 1, yMin - 1));
                zeroLightValue(xMax + 1, yMin - 1);
            }
            for (int i = xMin; i <= xMax; i++) {
                sources.add(getLightingPoint(i, yMin - 1));
                zeroLightValue(i, yMin - 1);
            }
        }
        if (bufferBottom) {
            if (bufferLeft) {
                sources.add(getLightingPoint(xMin - 1, yMax + 1));
                zeroLightValue(xMin - 1, yMax + 1);
            }
            if (bufferRight) {
                sources.add(getLightingPoint(xMax + 1, yMax + 1));
                zeroLightValue(xMax + 1, yMax + 1);
            }
            for (int i = xMin; i <= xMax; i++) {
                sources.add(getLightingPoint(i, yMax + 1));
                zeroLightValue(i, yMax + 1);
            }
        }
        if (bufferLeft) {
            for (int i = yMin; i <= yMax; i++) {
                sources.add(getLightingPoint(xMin - 1, i));
                zeroLightValue(xMin - 1, i);
            }
        }
        if (bufferRight) {
            for (int i = yMin; i <= yMax; i++) {
                sources.add(getLightingPoint(xMax + 1, i));
                zeroLightValue(xMax + 1, i);
            }
        }
        for (int i = xMin; i <= xMax; i++) {
            for (int j = yMin; j <= yMax; j++) {
                if (isSource(i, j))
                    sources.add(getLightingPoint(i, j));

                zeroLightValue(i, j);
            }
        }
        spreadLighting(sources);
    }

    private void spreadLighting(List<LightingPoint> sources) {
        if (sources.isEmpty())
            return;

        PriorityQueue<LightingPoint> in = new PriorityQueue<>(sources.size(), LIGHT_VALUE_COMPARATOR);
        out.clear();

        out.addAll(sources);

        in.addAll(sources);

        while (!in.isEmpty()) {
            LightingPoint current = in.poll();
            out.add(current);

            try {
                if (current.getLightValue() <= lightLevels[current.getX()][current.getY()] || current.getLightValue() < 0)
                    continue;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                continue;
            }

            lightLevels[current.getX()][current.getY()] = current.getLightValue();
            sourceMap[current.getX()][current.getY()] = current.isSource();

            List<LightingPoint> neighbors = current.getNeighbors();
            for (LightingPoint next : neighbors) {
                if (out.contains(next))
                    continue;

                in.add(next);
            }
        }
    }

    private List<LightingPoint> getSunSources(int x) {
        List<LightingPoint> sources = new ArrayList<>();
        for (int y = 0; y < world.getHeight() - 1; y++) {
            if (world.getBlock(x, y).getLightBlocking() != 0)
                break;

            sources.add(new LightingPoint(world, x, y, lightValueSun, true));
        }
        return sources;
    }

    private LightingPoint getLightingPoint(int x, int y) {
        return new LightingPoint(world, x, y, lightLevels[x][y], sourceMap[x][y]);
    }

    private void zeroLightValue(int x, int y) {
        if (x < 0 || y < 0 || x > (lightLevels.length - 1) || y > (lightLevels[0].length - 1))
            return;
        lightLevels[x][y] = 0;
    }

    public int getLightValue(int x, int y) {
        if (x < 0 || y < 0 || x > (lightLevels.length - 1) || y > (lightLevels[0].length - 1))
            return 0;
        return lightLevels[x][y];
    }

    public boolean isSource(int x, int y) {
        if (x < 0 || y < 0 || x > (lightLevels.length - 1) || y > (lightLevels[0].length - 1))
            return false;
        return sourceMap[x][y];
    }
}
