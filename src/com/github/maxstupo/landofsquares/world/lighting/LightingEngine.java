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

        this.init();
    }

    private void init() {
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
                    }

                }
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

            if (current.getLightValue() <= lightLevels[current.getX()][current.getY()] || current.getLightValue() < 0)
                continue;

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
